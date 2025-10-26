package com.hexalyte.salon.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class BranchAccessControlAspect {

    @Autowired
    private BranchAccessControlService branchAccessControlService;

    @Around("@annotation(requireBranchAccess)")
    public Object checkBranchAccess(ProceedingJoinPoint joinPoint, RequireBranchAccess requireBranchAccess) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        // Find the branch ID parameter
        Long branchId = extractBranchId(method, args, requireBranchAccess.branchIdParam());
        
        if (branchId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Branch ID not found in method parameters");
        }

        // Check access based on the required level
        boolean hasAccess = checkAccessLevel(branchId, requireBranchAccess.level(), requireBranchAccess.allowAdmin());
        
        if (!hasAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access denied: Insufficient permissions for this branch");
        }

        return joinPoint.proceed();
    }

    private Long extractBranchId(Method method, Object[] args, String paramName) {
        Parameter[] parameters = method.getParameters();
        
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equals(paramName) && args[i] instanceof Long) {
                return (Long) args[i];
            }
        }
        
        return null;
    }

    private boolean checkAccessLevel(Long branchId, RequireBranchAccess.BranchAccessLevel level, boolean allowAdmin) {
        switch (level) {
            case VIEW:
                return branchAccessControlService.hasBranchAccess(branchId);
            case MANAGE:
                return branchAccessControlService.hasBranchRole(branchId, 
                    com.hexalyte.salon.model.BranchUserAccess.AccessRole.MANAGER) ||
                   (allowAdmin && branchAccessControlService.canManageBranches());
            case FINANCIAL:
                return branchAccessControlService.canViewFinancialReports(branchId) ||
                   (allowAdmin && branchAccessControlService.canManageBranches());
            case STAFF_MANAGEMENT:
                return branchAccessControlService.canManageStaff(branchId) ||
                   (allowAdmin && branchAccessControlService.canManageBranches());
            default:
                return false;
        }
    }
}
