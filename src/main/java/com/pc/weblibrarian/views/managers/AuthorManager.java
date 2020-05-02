package com.pc.weblibrarian.views.managers;

import com.pc.weblibrarian.customcomponents.*;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.AuthorDP;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.enums.PersonGenderType;
import com.pc.weblibrarian.security.SecuredByRole;
import com.pc.weblibrarian.utils.CustomNullChecker;
import com.pc.weblibrarian.views.BasicDialog;
import com.pc.weblibrarian.views.MainFrame;
import com.pc.weblibrarian.views.dialogs.AuthorsCRUDDialog;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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

//@PageTitle("Library Authors")

// @PreAuthorize(value = "hasAnyRole('USER','ADMIN', 'SUPER_ADMIN')")
@SecuredByRole(value = {"USER", "ADMIN"})
@Route(value = "authors", layout = MainFrame.class)
@UIScope
// @PreserveOnRefresh
public class AuthorManager extends Fragment
{
    private static final boolean superadmin = true;
    
    private AuthorDP dp = new AuthorDP();
    private ConfigurableFilterDataProvider<Author, Void, String> configurableFilterDataProvider = dp.withConfigurableFilter();
    
    Grid<Author> grid = new Grid<>(Author.class, false);
    
    Author aa = new Author();
    
    int total;
    
    public AuthorManager()
    {
        super();
        total = configurableFilterDataProvider.size(new Query<>());
        
        Span totalcountspan = new Span(total + " Total");
        totalcountspan.addClassNames("secondary-text", "font-size-xs");
        configurableFilterDataProvider.addDataProviderListener(lst -> totalcountspan.setText(total + " Total"));
        
        CustomSearchTextField searchbox = new CustomSearchTextField("Search", ValueChangeMode.EAGER, new Icon("lumo", "search"), false/*not needed here with TextFieldClearButton in use*/, "Search By Name", "small");
        searchbox.addValueChangeListener(event ->
                                         {
                                             configurableFilterDataProvider.setFilter(event.getValue());
                                             configurableFilterDataProvider.refreshAll();
            
                                             total = configurableFilterDataProvider.size(new Query<>());
                                             totalcountspan.setText(total + " Total");
                                         });
        
        grid.setDataProvider(configurableFilterDataProvider);
        grid.setMultiSort(true);
        grid.setColumnReorderingAllowed(true);
        
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        GridSingleSelectionModel<Author> singleSelect = (GridSingleSelectionModel<Author>) grid.getSelectionModel();
        singleSelect.asSingleSelect();
        //singleSelect.setDeselectAllowed(true);
        
        // grid.onEnabledStateChanged(true);
        // grid.addSelectionListener(e -> grid.getDataProvider().refreshAll());
        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(author ->
                                        {
                                            VerticalLayout layout = new VerticalLayout();
                                            //List<Address> addresses = author.getPerson().getAddressIds().stream().map(aa -> GetObjectByID.getObjectById(aa, Connection.addresses)).collect(Collectors.toList());
                                            //author.getPerson().setAddressObjects(addresses);
                                            if (author.getPerson().getAddressObjects().size() > 0)
                                            {
                                                author.getPerson().getAddressObjects()
                                                      .forEach(address ->
                                                               {
                                                                   if (!CustomNullChecker.nullObjectChecker(address))
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
        
        /*grid.addItemClickListener(d ->
                                  {
                                      
                                      Author p = d.getItem();
            
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
                                          grid.getDataProvider().refreshAll();
                                      }
                                      else
                                      {
                                          grid.setDetailsVisible(p, true);
                                          grid.getDataProvider().refreshAll();
                                      }
                                  });*/
        
        /*singleSelect.addSingleSelectionListener(ssl ->
                                                {
                                                    try
                                                    {
                                                        ssl.getAllSelectedItems().forEach(c ->
                                                                                          {
                                                                                              if (grid.isDetailsVisible(c))
                                                                                              {
                                                                                                  grid.setDetailsVisible(c, false);
                                                                                              }
                                                                                          });
                                                        if (!grid.isDetailsVisible(ssl.getValue()) && ssl.getValue().getPerson().getAddressObjects().size() > 0)
                                                        {
                                                            grid.getDataProvider().refreshAll();
                                                            grid.setDetailsVisible(ssl.getValue(), true);
                                                        }
                                                    }
                                                    catch (Exception e)
                                                    {
                
                                                    }
                                                });*/
        
        grid.addColumn(new ComponentRenderer<>(a ->
                                               {
                                                   if (a.getPerson().getGender() == PersonGenderType.MALE)
                                                   {
                                                       return new Icon(VaadinIcon.MALE);
                                                   }
                                                   else
                                                   {
                                                       return new Icon(VaadinIcon.FEMALE);
                                                   }
                                               })).setHeader(new Html("<b>Gender</b>"))/*.setKey("gender")*/;
        Grid.Column<Author> biography = grid.addColumn(Author::getBiography).setHeader("Biography").setResizable(true);
        Grid.Column<Author> wikiLink = grid.addColumn(Author::getWikiLink).setHeader("WikiLink").setResizable(true);
        
        grid.addColumn(a -> a.getPerson().getTitle()).setHeader("Title").setResizable(true);
        Grid.Column<Author> first_name = grid.addColumn(a -> a.getPerson().getFirstName(), "firstName")
                                             .setHeader("First Name").setSortProperty("firstName").setResizable(true)/** TODO> feature */
                                             .setComparator(Comparator.comparing(a -> a.getPerson().getFirstName().toLowerCase()));
        
        Grid.Column<Author> last_name = grid.addColumn(a -> a.getPerson().getLastName(), "lastName")
                                            .setHeader("Last Name").setSortProperty("lastName").setResizable(true).setComparator(Comparator.comparing(a -> a.getPerson().getLastName().toLowerCase()));
        grid.addColumn(a -> a.getPerson().getEmailAddress()).setHeader("Email").setResizable(true);
        grid.addColumn(a -> a.getPerson().getPhoneNumber()).setHeader("Phone").setResizable(true);
        grid.addColumn(new LocalDateRenderer<>(a -> a.getPerson().getDateOfBirth(), "dd/MM/yyyy")).setHeader("Date of Birth").setResizable(true);
        grid.addColumn(TemplateRenderer.<Author>of("[[item.age]] years old")
                               .withProperty("age", a -> Year.now().getValue() - a.getPerson().getDateOfBirth().getYear()), "age").setHeader("Age");
        
        // Grid<Author> finalgrid = grid;
        grid.addComponentColumn(author ->
                                {
            
                                    try
                                    {
                                        aa = (Author) author.clone();
                                    }
                                    catch (CloneNotSupportedException e)
                                    {
                                        e.getMessage();
                                    }
                                    Div fl = new Div();
                                    fl.getStyle().set("textAlign", "right");
            
                                    Button edit = new Button(new IronIcon("lumo", "edit"));
                                    edit.getElement().setAttribute("theme", "small icon");
                                    edit.getStyle().set("marginRight", "0.4em");
                                    edit.addClickListener(click ->
                                                          {
                                                              new AuthorsCRUDDialog(author, superadmin ? BasicDialog.BeanAction.EDIT : BasicDialog.BeanAction.VIEW, (a) ->
                                                              {
                                                                  // grid.getDataProvider().refreshItem(a);
                                                                  configurableFilterDataProvider.refreshItem(a);
                                                                  grid.getDataProvider().refreshAll();
                                                                  UI.getCurrent().getPage().reload();
                                                                  return true;
                                                              }, BasicDialog.DialogSize.MID).open();
                                                          });
                                    Button delete = new Button(new IronIcon("lumo", "cross"));
                                    delete.getElement().setAttribute("theme", "small icon error");
                                    delete.addClickListener(click ->
                                                            {
                                                                deleteAuthorView(author, grid);
                                                            });
            
                                    fl.add(edit, delete);
                                    return fl;
                                }).setFlexGrow(0).setWidth("100px").setResizable(true);
        
        SmallButton edit = HTMLComponents.getEditButton();
        
        //boolean superadmin = MainFrame.getUserRoleType().isSuperAdmin();
        
        HeaderRow topRow = grid.prependHeaderRow();

// group two columns under the same label
        topRow.join(first_name, last_name).setComponent(new Label("Person Information"));

// group the other two columns in the same header row
        topRow.join(biography, wikiLink).setComponent(new Label("Author Information"));
        
        addHeaderComponent(new H1("Fragment Main Header"));
        addHeaderComponent(new H3("Authors Manager"));
        
        // TODO> search fo icons and themes using Variant, VaadinIcon.ADD_DOCK API
        SmallButton addentity = new SmallButton("New Author").theme("primary");
//        addentity.setIcon(new Icon("lumo", "add"));
        addentity.setIcon(new Icon(VaadinIcon.ADD_DOCK));
        //addentity.setVisible(MainFrame.getUserRoleType().isSuperAdmin());
        
        addentity.addClickListener(clk -> new AuthorsCRUDDialog(new Author(),
                                                                BasicDialog.BeanAction.NEW,
                                                                (a) ->
                                                                {
                                                                    //refresh data
                                                                    //refreshDataTable(a);
                                                                    configurableFilterDataProvider.refreshItem(a);
                                                                    grid.getDataProvider().refreshItem(a);
                                                                    grid.getDataProvider().refreshAll();
                                                                    UI.getCurrent().getPage().reload();
                                                                    return true;
                                                                }, BasicDialog.DialogSize.LARGE).open());
        
        
        appendHeaderBComponent(new HeaderControls(searchbox, addentity));
        addFooterComponent(totalcountspan);

//        addHeaderBComponent(new H2("Fragment Sub header"));
        addToolbarComponent(new Paragraph("You can sort, edit and delete records on this grid."));
        
        addContent(grid);
        addFooterComponent(new H6("Fragment Footer"));
    }
    
    private static void deleteAuthorView(Author author, Grid<Author> grid)
    {
        new CustomNotification("Delete Clicked", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
        
        String fullname = author.getPerson().getFirstName() + " " + author.getPerson().getLastName();
        new ConfirmDialog("Delete Author",
                          String.format("Are you sure you want to delete %s?", fullname), "Yes " + BasicDialog.BeanAction.DELETE.displayText(),
                          (e) ->
                          {
                              author.delete(Connection.DB_AUTHOR, author);
                              System.out.printf("Deleted Author: %s\n", fullname);
                              System.out.println("Author deleted successfully");
                              new CustomNotification("Author deleted successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
            
                              grid.getDataProvider().refreshItem(author);
                              grid.getDataProvider().refreshAll();
            
                              e.getSource().close();
                          }, "Do not delete", (e) ->
                          {
                              new CustomNotification("Author not deleted", "success", true, 1000, Notification.Position.TOP_CENTER).open();
            
                              System.out.println("Author not deleted");
                              e.getSource().close();
                          }).open();
    }
    
    private void refreshDataTable(Author t)
    {
        configurableFilterDataProvider.refreshItem(t);
        grid.getDataProvider().refreshAll();
    }
}
