package com.pc.weblibrarian.views.users;

import com.pc.weblibrarian.customcomponents.Fragment;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataproviders.LibraryUsersDP;
import com.pc.weblibrarian.entity.LibraryUser;
import com.pc.weblibrarian.views.MainFrame;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("User Registration")
@Route(value = "users", layout = MainFrame.class)
public class UserRegistration extends Fragment
{
    
    private static final long serialVersionUID = 1L;
    
    private LibraryUsersDP dp = new LibraryUsersDP();
    
    private ConfigurableFilterDataProvider<LibraryUser, Void, String> fdp = dp.withConfigurableFilter();
    
    int total;
    
    public UserRegistration()
    {
        super();
        setHeaderText("Users and Roles");
        
        total = fdp.size(new Query<>());
        SmallButton adduserbtn = new SmallButton("Register New User");
        adduserbtn.theme("primary");
        
    }
}
