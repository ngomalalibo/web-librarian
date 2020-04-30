package com.pc.weblibrarian.customcomponents;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.entity.Address;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.enums.PersonGenderType;
import com.pc.weblibrarian.views.BasicDialog;
import com.pc.weblibrarian.views.dialogs.AuthorsCRUDDialog;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class AuthorGrid
{
    private static final boolean superadmin = true;
    
    public static Grid<Author> getGrid(Grid<Author> gridd)
    {
        gridd.setMultiSort(true);
        gridd.setSelectionMode(Grid.SelectionMode.SINGLE);
        gridd.setColumnReorderingAllowed(true);
        // gridd.setVerticalScrollingEnabled(true);
        /*gridd.addItemClickListener(listener ->
                                   {
                                       
                                       gridd.select(listener.getItem());
                                       gridd.setDetailsVisible(listener.getItem(), true);
                                       
                                   });
        gridd.addSelectionListener(d ->
                                   {
                                       d.getAllSelectedItems().forEach(e -> gridd.setDetailsVisible(e, false));
                                       gridd.deselectAll();
                                   });*/
        
        //sets the fetch limit to the dataprovider per page
//        grid.setPageSize(25);

// TODO feature>  grid.removeColumnByKey("gender");
// TODO feature> preselect value grid.select(defaultItem);
        
        GridSingleSelectionModel<Author> singleSelect = (GridSingleSelectionModel<Author>) gridd.getSelectionModel();
        singleSelect.asSingleSelect();

// disallow empty selection
        //singleSelect.setDeselectAllowed(true);
        
        
        //listener on single selected item
        /*grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addItemClickListener(event -> System.out
                .println(("Clicked Item: " + event.getItem())));*/
        
        /*grid.addComponentColumn(author ->
                                {
                                    Checkbox checkBox = new Checkbox();
                                    checkBox.addValueChangeListener(e ->
                                                                    {
                                                                        singleSelect.addSingleSelectionListener(se ->
                                                                                                                {
                    
                                                                                                                    Optional<AuthorCommand> selectedItem = se.getSelectedItem();
                                                                                                                    //singleSelect.deselectAll();
                                                                                                                    //singleSelect.select(selectedItem.get());
                                                                                                                });
                                                                    });
                                    return checkBox;
                                }).setHeader("Select")*//** TODO> feature .setVisible(true)*//*;*/
        gridd.addColumn(new ComponentRenderer<>(a ->
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
        Grid.Column<Author> biography = gridd.addColumn(Author::getBiography).setHeader("Biography").setResizable(true);
        Grid.Column<Author> wikiLink = gridd.addColumn(Author::getWikiLink).setHeader("WikiLink").setResizable(true);
        
        
        gridd.addColumn(a -> a.getPerson().getTitle()).setHeader("Title").setResizable(true);
        Grid.Column<Author> first_name = gridd.addColumn(a -> a.getPerson().getFirstName(), "firstName")
                                              .setHeader("First Name").setSortProperty("firstName")/*.setSortOrderProvider(direction -> Arrays
                .asList(new QuerySortOrder("firstName", direction),
                        new QuerySortOrder("lastName", direction))
                .stream())*/.setResizable(true)/** TODO> feature */
                                              .setComparator(Comparator.comparing(a -> a.getPerson().getFirstName().toLowerCase()));
        
        Grid.Column<Author> last_name = gridd.addColumn(a -> a.getPerson().getLastName(), "lastName")
                                             .setHeader("Last Name").setSortProperty("lastName").setResizable(true).setComparator(Comparator.comparing(a -> a.getPerson().getLastName().toLowerCase()));
        gridd.addColumn(a -> a.getPerson().getEmailAddress()).setHeader("Email").setResizable(true);
        gridd.addColumn(a -> a.getPerson().getPhoneNumber()).setHeader("Phone").setResizable(true);
        gridd.addColumn(new LocalDateRenderer<>(a -> a.getPerson().getDateOfBirth(), "dd/MM/yyyy")).setHeader("Date of Birth").setResizable(true);
        gridd.addColumn(TemplateRenderer.<Author>of("[[item.age]] years old")
                                .withProperty("age", a -> Year.now().getValue() - a.getPerson().getDateOfBirth().getYear()), "age").setHeader("Age");
        
        Grid<Author> finalGridd = gridd;
        gridd.addComponentColumn(author ->
                                 {
                                     Div fl = new Div();
                                     fl.getStyle().set("textAlign", "right");
            
                                     Button edit = new Button(new IronIcon("lumo", "edit"));
                                     edit.getElement().setAttribute("theme", "small icon");
                                     edit.getStyle().set("marginRight", "0.4em");
                                     edit.addClickListener(click ->
                                                           {
                                                               new AuthorsCRUDDialog(author, superadmin ? BasicDialog.BeanAction.EDIT : BasicDialog.BeanAction.VIEW, (a) ->
                                                               {
                                                                   finalGridd.getDataProvider().refreshItem(a);
                                                                   finalGridd.getDataProvider().refreshAll();
                                                                   ///gridd.getDataProvider().refreshAll();
                                                                   return true;
                                                               }, BasicDialog.DialogSize.MID).open();

//                                                              editAuthorView(author);
                                                               //grid.getDataProvider().refreshAll();
                                                           });
                                     Button delete = new Button(new IronIcon("lumo", "cross"));
                                     delete.getElement().setAttribute("theme", "small icon error");
                                     delete.addClickListener(click ->
                                                             {
                                                                 deleteAuthorView(author, finalGridd);
                                                             });
            
                                     fl.add(edit, delete);
                                     return fl;
                                 }).setFlexGrow(0).setWidth("100px").setResizable(true);
        
        SmallButton edit = HTMLComponents.getEditButton();
        
        //boolean superadmin = MainFrame.getUserRoleType().isSuperAdmin();
        
        /*grid.addColumn(TemplateRenderer.<AuthorCommand>of(edit.getElement().getOuterHTML()).withEventHandler("edit",
                                                                                                             authorCommand -> new NewAuthorsDialog(CommandConverters.authorCommandToAuthor(authorCommand),
                                                                                                                                                   superadmin ? BasicDialog.BeanAction.EDIT : BasicDialog.BeanAction.VIEW, (a) ->
                                                                                                                                                   {
                                                                                                                                                       grid.getDataProvider().refreshItem(authorCommand);
                                                                                                                                                       //filterDP.refreshAll();
                                                                                                                                                       return true;
                                                                                                                                                   }, BasicDialog.DialogSize.MID).open())).setFlexGrow(0).setWidth(HTMLComponents.editButtonWidth).setResizable(true);*/
        gridd.setItemDetailsRenderer(
                new ComponentRenderer<>(author ->
                                        {
                                            VerticalLayout layout = new VerticalLayout();
                                            List<Address> addresses = author.getPerson().getAddressIds().stream().map(aa -> GetObjectByID.getObjectById(aa, Connection.addresses)).collect(Collectors.toList());
                                            //authorCommand.getPerson().getAddressIds().forEach(d -> addresses.add(GetObjectByID.getObjectById(d, Connection.addresses))); //replaced with line above
                                            author.getPerson().setAddressObjects(addresses);
                                            author.getPerson().getAddressObjects()
                                                  .forEach(address -> layout.add(new Label("Address: "
                                                                                                   + address.getStreetAddressLine() + " "
                                                                                                   + address.getCity() + " "
                                                                                                   + address.getState() + " "
                                                                                                   + address.getCountry())));
                                            return layout;
                                        }));
        
        HeaderRow topRow = gridd.prependHeaderRow();

// group two columns under the same label
        topRow.join(first_name, last_name).setComponent(new Label("Person Information"));

// group the other two columns in the same header row
        topRow.join(biography, wikiLink).setComponent(new Label("Author Information"));
        
        // doesnt work grid.setSortableColumns("firstName", "lastName");
        
        /*Icon picon = new Icon("social", "person");
        picon.setSize("80%");
        
        Span personicon = new Span(picon);
        personicon.setClassName("personicon");
        
        Div img = new Div();
        
        img.getStyle().set("position", "absolute").set("backgroundSize", "contain");
        img.setClassName("userg-image");
        
        Div combo = new Div(personicon, img);
        combo.setClassName("userg-partb16");
        
        Span parta = new Span("[[item.namez]]");
        parta.setClassName("userg-parta");
        
        Div partc = new Div(combo, parta);
        partc.setClassName("userg-partc");*/
        
        
        return gridd;
        
        //grid.removeColumnByKey("id");

// The Grid<>(Person.class) sorts the properties and in order to
// reorder the properties we use the 'setColumns' method.
//        grid.setColumns("firstName", "lastName", "age", "address",
//                        "phoneNumber");
    
    }
    
    private static void deleteAuthorView(Author author, Grid<Author> gridd)
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
                              
                              gridd.getDataProvider().refreshItem(author);
                              gridd.getDataProvider().refreshAll();
            
                              e.getSource().close();
                          }, "Do not delete", (e) ->
                          {
                              new CustomNotification("Author not deleted", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                              
                              System.out.println("Author not deleted");
                              e.getSource().close();
                          }).open();
    }
    
    /*private static void editAuthorView(Author author)
    {
        new NewAuthorsDialog(author, superadmin ? BasicDialog.BeanAction.EDIT : BasicDialog.BeanAction.VIEW, (a) ->
        {
            grid.getDataProvider().refreshItem(CommandConverters.authorToAuthorCommand(author));
            //filterDP.refreshAll();
            return true;
        }, BasicDialog.DialogSize.MID).open();
    }*/
}
