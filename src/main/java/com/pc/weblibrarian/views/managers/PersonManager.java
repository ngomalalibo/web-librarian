package com.pc.weblibrarian.views.managers;

import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.CustomSearchTextField;
import com.pc.weblibrarian.customcomponents.Fragment;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.dataproviders.PersonDP;
import com.pc.weblibrarian.entity.Address;
import com.pc.weblibrarian.entity.Person;
import com.pc.weblibrarian.enums.PersonGenderType;
import com.pc.weblibrarian.security.SecuredByRole;
import com.pc.weblibrarian.views.BasicDialog;
import com.pc.weblibrarian.views.MainFrame;
import com.pc.weblibrarian.views.dialogs.PersonCRUDDialog;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SecuredByRole(value = {"USER", "ADMIN"})
@Route(value = "persons", layout = MainFrame.class)
@UIScope
//@ParentLayout(MainFrame.class)
// @PreserveOnRefresh
public class PersonManager extends Fragment
{
    private PersonDP dp = new PersonDP();
    private ConfigurableFilterDataProvider<Person, Void, String> configurableFilterDataProvider = dp.withConfigurableFilter();
    
    private Grid<Person> grid = new Grid<>(Person.class, false);
    private static final boolean superadmin = true;
    
    private int total;
    
    public PersonManager()
    {
        super();
        grid.setDataProvider(configurableFilterDataProvider);
        grid.setMultiSort(true);
        grid.setColumnReorderingAllowed(true);
        
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        GridSingleSelectionModel<Person> singleSelect = (GridSingleSelectionModel<Person>) grid.getSelectionModel();
        singleSelect.asSingleSelect();
        singleSelect.setDeselectAllowed(true);
        
        /*grid.addItemClickListener(d ->
                                  {
                                      Person p = d.getItem();
            
                                      if (grid.getSelectedItems().size() > 0)
                                      {
                                          grid.getSelectedItems().forEach(s ->
                                                                          {
                                                                              grid.setDetailsVisible(s, false);
                                                                          });
                                          grid.getDataProvider().refreshAll();
                                          grid.deselectAll();
                                      }
                                      grid.select(p);
                                      if (grid.isDetailsVisible(p))
                                      {
                                          grid.setDetailsVisible(p, false);
                                      }
                                      else
                                      {
                                          grid.setDetailsVisible(p, true);
                                      }
                                      grid.getDataProvider().refreshAll();
                                  });
        */
        /*singleSelect.addSingleSelectionListener(ssl ->
                                                {
                                                    try
                                                    {
                                                        Person p = (Person)ssl.getValue().clone();
                                                        if (!ssl.getAllSelectedItems().isEmpty())
                                                        {
                                                            ssl.getAllSelectedItems().forEach(c ->
                                                                                              {
                                                                                                  grid.setDetailsVisible(c, false);
                                                                                              });
                                                            
                                                        }
                                                        if (!grid.isDetailsVisible(p) && ssl.getValue().getAddressObjects().size() > 0)
                                                        {
                                                            grid.getDataProvider().refreshAll();
                                                            grid.setDetailsVisible(p, true);
                                                            // grid.getDataProvider().refreshItem(ssl.getValue(), true);
                                                            //grid.getDataProvider().refreshItem(ssl.getValue());
                                                        }
                                                        else
                                                        {
                                                            grid.getDataProvider().refreshAll();
                                                            ssl.getFirstSelectedItem().ifPresent(d-> grid.setDetailsVisible(d, true));
                                                        }
                                                    }
                                                    catch (Exception e)
                                                    {
                
                                                    }
                                                });*/
        
        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(pc ->
                                        {
                                            VerticalLayout layout = new VerticalLayout();
                                            List<Address> addresses = pc.getAddressIds().stream().map(d -> GetObjectByID.getObjectById(d, Connection.addresses)).collect(Collectors.toList());
                    
                    
                                            pc.setAddressObjects(addresses);
                                            if (pc.getAddressObjects().size() > 0)
                                            {
                                                pc.getAddressObjects().forEach(address ->
                                                                               {
                                                                                   if (address != null)
                                                                                   {
                                                                                       layout.add(new Label("Address: "
                                                                                                                    + address.getStreetAddressLine() + ", "
                                                                                                                    + address.getCity() + ", "
                                                                                                                    + address.getState().getRegion() + ", "
                                                                                                                    + address.getCountry().getName()));
                                                                                   }
                                                                               });
                                            }
                                            return layout;
                                        }));


// TODO feature>  grid.removeColumnByKey("gender");
// TODO feature> preselect value grid.select(defaultItem);
        
        // disallow empty selection
        
        
        /** TODO> feature .setVisible(true)*/
        grid.addColumn(new ComponentRenderer<>(a ->
                                               {
                                                   if (a.getGender() == PersonGenderType.MALE)
                                                   {
                                                       return new Icon(VaadinIcon.MALE);
                                                   }
                                                   else
                                                   {
                                                       return new Icon(VaadinIcon.FEMALE);
                                                   }
                                               })).setHeader(new Html("<b>Gender</b>"));
        
        grid.addColumn(Person::getTitle).setHeader("Title").setResizable(true);
        Grid.Column<Person> first_name = grid.addColumn(Person::getFirstName, "firstName")
                                             .setHeader("First Name").setSortProperty("firstName").setResizable(true)
                                             .setComparator(Comparator.comparing(a -> a.getFirstName().toLowerCase()));
        
        Grid.Column<Person> last_name = grid.addColumn(Person::getLastName, "lastName")
                                            .setHeader("Last Name").setSortProperty("lastName").setResizable(true)
                                            .setComparator(Comparator.comparing(a -> a.getLastName().toLowerCase()));
        grid.addColumn(Person::getEmailAddress).setHeader("Email").setResizable(true);
        grid.addColumn(Person::getPhoneNumber).setHeader("Phone").setResizable(true);
        grid.addColumn(new LocalDateRenderer<>(Person::getDateOfBirth, "dd/MM/yyyy")).setHeader("Date of Birth").setResizable(true);
        grid.addColumn(TemplateRenderer.<Person>of("[[item.age]] years old").withProperty("age", a -> Year.now().getValue() - a.getDateOfBirth().getYear()), "age").setHeader("Age");
        
        Grid<Person> finalgrid = grid;
        grid.addComponentColumn(person ->
                                {
                                    Div fl = new Div();
                                    fl.getStyle().set("textAlign", "right");
            
                                    Button edit = new Button(new IronIcon("lumo", "edit"));
                                    edit.getElement().setAttribute("theme", "small icon");
                                    edit.getStyle().set("marginRight", "0.4em");
                                    edit.addClickListener(click ->
                                                          {
                                                              new PersonCRUDDialog(person,
                                                                                   superadmin ? BasicDialog.BeanAction.EDIT : BasicDialog.BeanAction.VIEW,
                                                                                   (a) ->
                                                                                   {
                                                                                       finalgrid.getDataProvider().refreshItem(person);
                                                                                       finalgrid.getDataProvider().refreshAll();
                                                                                       UI.getCurrent().getPage().reload();
                                                                                       return true;
                                                                                   }).open();
                                                          });
                                    Button delete = new Button(new IronIcon("lumo", "cross"));
                                    delete.getElement().setAttribute("theme", "small icon error");
                                    delete.addClickListener(click ->
                                                            {
                                                                new CustomNotification("Delete clicked", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                                                new ConfirmDialog("Delete Person",
                                                                                  String.format("Are you sure you want to delete %s?", person.getFullName()), "Yes " + BasicDialog.BeanAction.DELETE.displayText(),
                                                                                  (e) ->
                                                                                  {
                                                                                      List<Address> addresses = person.getAddressIds().stream().map(d -> GetObjectByID.getObjectById(d, Connection.addresses)).collect(Collectors.toList());
                                                                                      if (addresses.size() > 0)
                                                                                      {
                                                                                          person.deleteMany(addresses, Connection.DB_ADDRESS);
                                                                                      }
                                                                                      person.delete(Connection.DB_PERSON, person);
                                                                                      System.out.printf("Deleted Person: %s\n", person.getFullName());
                                                                                      System.out.println("Person deleted successfully");
                                                                                      new CustomNotification("Person deleted successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                                                                      grid.getDataProvider().refreshItem(person);
                                                                                      grid.getDataProvider().refreshAll();
                    
                                                                                      e.getSource().close();
                                                                                  }, "Do not delete", (e) ->
                                                                                  {
                                                                                      new CustomNotification("Person not deleted", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                                                                      System.out.println("Person not deleted");
                                                                                      e.getSource().close();
                                                                                  }).open();
                                                            });
            
                                    fl.add(edit, delete);
                                    return fl;
                                }).setFlexGrow(0).setWidth("100px").setResizable(true);
        
        // SmallButton edit = HTMLComponents.getEditButton();
        
        //boolean superadmin = MainFrame.getUserRoleType().isSuperAdmin();
        
        HeaderRow topRow = grid.prependHeaderRow();

// group two columns under the same label
        topRow.join(first_name, last_name).setComponent(new Label("Person Information"));

//        addHeaderComponent(new H1("Fragment Main Header"));
//        addHeaderComponent(new H3("Authors Manager"));
        
        addHeaderComponent(new H1("Fragment Main Header"));
        addHeaderComponent(new H3("People Manager"));
        
        // TODO> search for icons and themes using Variant, VaadinIcon.ADD_DOCK API
//        SmallButton addentity = new SmallButton("New Person").theme("primary");
        SmallButton addentity = new SmallButton("New Person").theme(ButtonVariant.LUMO_PRIMARY.getVariantName());
//        addentity.setIcon(new Icon("lumo", "add"));
        addentity.setIcon(new Icon(VaadinIcon.ADD_DOCK));
        //addentity.setVisible(MainFrame.getUserRoleType().isSuperAdmin());
        
        addentity.addClickListener(clk ->
                                   {
                                       // Person person = new Person();
                                       new PersonCRUDDialog(new Person(),
                                                            BasicDialog.BeanAction.NEW,
                                                            (a) ->
                                                            {
                                                                //refresh data
                                                                refreshDataTable((Person) a);
                                                                grid.getDataProvider().refreshItem((Person) a);
                                                                grid.getDataProvider().refreshAll();
                                                                UI.getCurrent().getPage().reload();
                                                                return true;
                                                            }).open();
                                   });
        
        total = configurableFilterDataProvider.size(new Query<>());
        
        Span totalcountspan = new Span(total + " Total");
        totalcountspan.addClassNames("secondary-text", "font-size-xs");
        configurableFilterDataProvider.addDataProviderListener(lst -> totalcountspan.setText(total + " Total"));
        
        CustomSearchTextField searchbox = new CustomSearchTextField("Search", ValueChangeMode.EAGER, new Icon("lumo", "search"), false/*not needed here with TextFieldClearButton in use*/, "Search By Name", "small");
        searchbox.addValueChangeListener(event ->
                                         {
                                             configurableFilterDataProvider.setFilter(searchbox.getValue());
                                             configurableFilterDataProvider.refreshAll();
            
                                             total = configurableFilterDataProvider.size(new Query<>());
                                             totalcountspan.setText(total + " Total");
                                         });
        
        appendHeaderBComponent(new HeaderControls(searchbox, addentity));
        addFooterComponent(totalcountspan);


//        addHeaderBComponent(new H2("Fragment Sub header"));
        addToolbarComponent(new Paragraph("You can sort, edit and delete records on this grid."));
        
        
        addContent(grid);
        addFooterComponent(new H6("Fragment Footer"));
    }
    
    private void refreshDataTable(Person p)
    {
        //listDataProvider = DataProvider.ofCollection(AuthorDataService.getAuthors().collect(Collectors.toList()));
        configurableFilterDataProvider.refreshItem(p);
        grid.getDataProvider().refreshAll();
    }
}
