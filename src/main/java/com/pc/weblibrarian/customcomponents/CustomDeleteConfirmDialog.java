package com.pc.weblibrarian.customcomponents;

import com.pc.weblibrarian.entity.PersistingBaseEntity;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

public class CustomDeleteConfirmDialog<T extends PersistingBaseEntity> extends ConfirmDialog
{
    /*AtomicReference<Div> message = new AtomicReference<>(new Div(new Span("Delete Clicked")));
    
    public CustomDeleteConfirmDialog(T bean, String classname, String property, String confirmText, ComponentEventListener<ConfirmEvent> confirmListener, String cancelText, ComponentEventListener<ConfirmDialog.CancelEvent> cancelListener)
    {
        super("Delete "+classname,
              String.format("Are you sure you want to delete %s?", property),
              "Yes "+ confirmText,
              (e) ->
              {
                  bean.delete(GetCollectionFromEntityName.getCollectionFromEntityName(classname).toString(), bean);
                  
                  System.out.println(classname+" deleted successfully");
                  message.set(new Div(new Span(classname+" deleted successfully")));
                  new CustomNotification(message.get()).open();
                  grid.getDataProvider().refreshItem(author);
                  grid.getDataProvider().refreshAll();
    
                  e.getSource().close();
              }confirmListener, cancelText, cancelListener);
        message.get().setClassName("error");
    
        new CustomDeleteConfirmDialog<>(bean,"Author",
                                      String.format("Are you sure you want to delete %s?", property),
                                      "Yes "+ BasicDialog.BeanAction.DELETE.displayText(),
                                      (e) ->
                                      {
                                          author.delete(Connection.DB_AUTHOR, author);
                                          System.out.printf("Deleted Author: %s\n", fullname);
                                          System.out.println("Author deleted successfully");
                                          message.set(new Div(new Span("Author deleted successfully")));
                                          new CustomNotification(message.get()).open();
                                          grid.getDataProvider().refreshItem(author);
                                          grid.getDataProvider().refreshAll();
    
                                          e.getSource().close();
                                      }
                                      )
        
        new ConfirmDialog("Delete Author",
                          String.format("Are you sure you want to delete %s?", fullname), "Yes " + BasicDialog.BeanAction.DELETE.displayText(),
                          (e) ->
                          {
                              author.delete(Connection.DB_AUTHOR, author);
                              System.out.printf("Deleted Author: %s\n", fullname);
                              System.out.println("Author deleted successfully");
                              message.set(new Div(new Span("Author deleted successfully")));
                              new CustomNotification(message.get()).open();
                              grid.getDataProvider().refreshItem(author);
                              grid.getDataProvider().refreshAll();
        
                              e.getSource().close();
                          }, "Do not delete", (e) ->
                          {
                              message.set(new Div(new Span("Author not deleted")));
                              System.out.println("Author not deleted");
//                                                            message.get().getStyle().set("color", "red");
                              new CustomNotification(message.get()).open();
                              e.getSource().close();
                          }).open();
    }*/
}
