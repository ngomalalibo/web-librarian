package com.pc.weblibrarian.enums;

import com.pc.weblibrarian.controllers.AuthorController;
import com.pc.weblibrarian.dataService.AuthorDataService;
import org.springframework.beans.factory.annotation.Autowired;

public enum PersonRoleType
{
    ROLE_USER, ROLE_ADMIN, ROLE_SUPERADMIN;
    
    public static String getDisplayText(PersonRoleType i)
    {
        switch (i)
        {
            case ROLE_USER:
                return "User";
            case ROLE_ADMIN:
                return "Admin";
            case ROLE_SUPERADMIN:
                return "Super_Admin";
            default:
                return "";
        }
    }
    
    public String displayText()
    {
        return getDisplayText(this);
    }
    
    public String displayColor()
    {
        switch (this)
        {
            case ROLE_USER:
                return "color-user";
            case ROLE_ADMIN:
                return "color-admin";
            case ROLE_SUPERADMIN:
                return "color-superadmin";
            default:
                return "";
        }
    }
    
    public boolean isSuperAdmin()
    {
        return ROLE_SUPERADMIN == this;
    }
    
    public boolean isAdmin()
    {
        return ROLE_ADMIN == this;
    }
    
    public boolean isUser()
    {
        return ROLE_USER == this;
    }
    
    public static PersonRoleType[] getAvailableRoles(PersonRoleType myrole)
    {
        switch (myrole)
        {
            case ROLE_SUPERADMIN:
                return new PersonRoleType[]{ROLE_USER, ROLE_ADMIN, ROLE_SUPERADMIN};
            case ROLE_ADMIN:
                return new PersonRoleType[]{ROLE_USER, ROLE_ADMIN};
            default:
                return new PersonRoleType[]{ROLE_USER};
        }
    }
    
    public boolean canDelete(PersonRoleType personRoleType, boolean addedbyme)
    {
        switch (this)
        {
            case ROLE_USER:
                return false;
            case ROLE_ADMIN:
                return personRoleType.isUser();
            case ROLE_SUPERADMIN:
                return personRoleType.isSuperAdmin() ? addedbyme : true;
        }
        return false;
    }
    
    @Autowired
    private static AuthorDataService authorRepo;
    
    public static void main(String[] args)
    {
        Object f = new AuthorController(authorRepo);
        System.out.println("f.getClass() = " + f.getClass());
        System.out.println("f.getClass() = " + f.getClass().getCanonicalName());
        // System.out.println(Arrays.stream(PersonRoleType.getAvailableRoles(PersonRoleType.ROLE_SUPERADMIN)).map(PersonRoleType::displayText).collect(Collectors.joining(", ")));
    }
}
