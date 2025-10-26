package com.hexalyte.salon.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireBranchAccess {
    
    /**
     * The branch ID parameter name in the method signature
     */
    String branchIdParam() default "branchId";
    
    /**
     * The required access level
     */
    BranchAccessLevel level() default BranchAccessLevel.VIEW;
    
    /**
     * Whether to allow admin users (default: true)
     */
    boolean allowAdmin() default true;
    
    enum BranchAccessLevel {
        VIEW,
        MANAGE,
        FINANCIAL,
        STAFF_MANAGEMENT
    }
}
