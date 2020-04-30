package com.pc.weblibrarian.views.managers;

import com.pc.weblibrarian.customcomponents.Fragment;
import com.pc.weblibrarian.security.SecuredByRole;

@SecuredByRole(value = {"USER", "ADMIN"})
public class CheckInCheckOutManager extends Fragment
{
    public CheckInCheckOutManager()
    {
        super();
    }
}
