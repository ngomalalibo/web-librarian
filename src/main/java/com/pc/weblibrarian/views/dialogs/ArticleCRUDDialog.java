package com.pc.weblibrarian.views.dialogs;

import com.pc.weblibrarian.customcomponents.*;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.AuthorDP;
import com.pc.weblibrarian.entity.Article;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.utils.BinderAuthorConverter;
import com.pc.weblibrarian.utils.CustomNullChecker;
import com.pc.weblibrarian.views.BasicDialog;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Emphasis;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.stream.Collectors;

public class ArticleCRUDDialog extends BasicDialog
{
    // TODO > Data providers
    AuthorDP dp = new AuthorDP();
    ConfigurableFilterDataProvider<Author, Void, String> fdp = dp.withConfigurableFilter();
    
    // TODO > Local Variables
    Article oldBean = null;
    
    // Article articleObj = new Article();
    
    // TODO > Layouts
    FormLayout articleFL = new FormLayout();
    FormLayout commentsFL = new FormLayout();
    HorizontalLayout outerLayoutHL = new HorizontalLayout(articleFL, commentsFL);
    
    HorizontalLayout saveCancelHL = new HorizontalLayout();
    // TODO > Labels
    Label titleH3 = new Label();
    Label personH2 = new Label();
    
    // TODO > Input Fields
    CustomNumberField rating = new CustomNumberField("Rating", "Rating", true, 0, 5, true, true);
    
    CustomTextField authorSearch = new CustomTextField("", ValueChangeMode.EAGER, new Icon(VaadinIcon.SEARCH), true, "Search Author", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    CustomTextField titleTF = new CustomTextField("Title", ValueChangeMode.EAGER, null, false, "Title", "medium");
    CustomTextArea articleTA = new CustomTextArea("Article",
                                                  ValueChangeMode.EAGER,
                                                  null,
                                                  false,
                                                  "Article",
                                                  TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    
    
    ListBox<Author> articleAuthorLB = new ListBox<>();
    // ListBox<Comment> commentLB = new ListBox<>();
    
    public ArticleCRUDDialog(Article bean, BasicDialog.BeanAction beanAction, BasicDialog.Action<Article> basicDialogAction, DialogSize dialogSiz)
    {
        super(beanAction, dialogSiz);
        
        titleH3.addClassName("mr-2");
        personH2.addClassName("ml-5");
        
        
        // TODO > Data Components. Grid, List
        articleAuthorLB.setDataProvider(fdp);
        articleAuthorLB.getElement().getStyle().set("size", "4px");
        articleAuthorLB.getElement().getStyle().set("border", "1px solid ivory");
        articleAuthorLB.setMaxHeight("150px");
        articleAuthorLB.setHeight("150px");
        articleAuthorLB.setWidthFull();
        
        /*commentLB.setItems(bean.getComments());
        commentLB.getElement().getStyle().set("size", "4px");
        commentLB.getElement().getStyle().set("border", "1px solid ivory");
        commentLB.setMaxHeight("150px");
        commentLB.setHeight("150px");
        commentLB.setWidthFull();*/
        
        personH2.addClassName("font-size-s");
        personH2.addClassName("text-primary");
        
        titleH3.addClassName("mr-2");
        personH2.addClassName("ml-5");
        
        // TODO > Positioning
        articleFL.setSizeFull();
        // commentsFL.setSizeFull();
        
        
        SmallButton save = new SmallButton("Save");
        save.theme("primary");
        
        SmallButton edit = new SmallButton("Edit");
        edit.theme("primary");
        
        SmallButton cancel = new SmallButton("Cancel");
        SmallButton clear = new SmallButton("Clear");
        SmallButton delete = new SmallButton("Delete");
        delete.theme(ButtonVariant.LUMO_ERROR.getVariantName());
        
        Binder<Article> binder = new Binder<>(Article.class, true);
        
        try
        {
            oldBean = (Article) bean.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.getMessage();
        }
        
        binder.setReadOnly(beanAction == BeanAction.DELETE || beanAction == BeanAction.VIEW);
        
        binder.forField(titleTF).bind("title");
        binder.forField(articleTA).bind("article");
        binder.forField(rating).bind("rating");
        binder.forField(articleAuthorLB).withConverter(new BinderAuthorConverter()).bind(Article::getAuthorID, Article::setAuthorID);
        //binder.setBean(bean);
        // binder.bind(articleAuthorLB, "authorID");
        
        try
        {
            if (beanAction != BeanAction.NEW)
            {
                if (bean.getAuthor() != null)
                {
                    articleAuthorLB.setValue(bean.getAuthor());
                    binder.readBean(bean);
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            e.getMessage();
        }
        
        //set binder for author LB
        binder.addStatusChangeListener(event ->
                                       {
                                           if (articleAuthorLB.getValue() != null)
                                           {
                                               if (titleTF.getValue().equals("") || articleTA.getValue().equals(""))
                                               {
                                                   save.setEnabled(false);
                                                   edit.setEnabled(false);
                    
                                                   //new CustomNotification("continue completing form details", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                               }
                                               else
                                               {
                                                   save.setEnabled(true);
                                                   edit.setEnabled(true);
                                                   getErrorDiv().setVisible(false);
                                                   getErrorDiv().setText("");
                                                   //new CustomNotification("Form is looking good", "primary", true, 1000, Notification.Position.TOP_CENTER).open();
                                               }
                                           }
                                           else
                                           {
                                               new CustomNotification("Select an author", "btn-outline-danger", false, 3000, Notification.Position.TOP_CENTER).open();
                                           }
                                       });
        
        
        /*if (titleTF.getValue().equals("") || articleTA.getValue().equals(""))
        {
            save.setEnabled(false);
            edit.setEnabled(false);
        }
        else
        {
            save.setEnabled(true);
            edit.setEnabled(true);
        }*/
        
        
        if (beanAction != BeanAction.VIEW)
        {
            articleAuthorLB.setEnabled(false);
            if (beanAction != BeanAction.DELETE)
            {
                if (beanAction == BeanAction.NEW)
                {
                    titleH3 = new Label("New Article");
                    setTitle(titleH3);
                    saveCancelHL.add(save);
                    
                    save.setVisible(true);
                    edit.setVisible(false);
                    
                    articleAuthorLB.setEnabled(true);
                }
                if (beanAction == BeanAction.EDIT)
                {
                    titleH3 = new Label("Edit Article");
                    setTitle(titleH3);
                    saveCancelHL.add(edit);
                    
                    save.setVisible(false);
                    edit.setVisible(true);
                    
                    articleAuthorLB.setEnabled(true);
                }
            }
            if ((beanAction == BeanAction.NEW || beanAction == BeanAction.EDIT))
            {
                saveCancelHL.add(clear);
                clear.setVisible(true);
                articleAuthorLB.setEnabled(true);
            }
            
            if (beanAction == BeanAction.DELETE)
            {
                saveCancelHL.add(delete);
                clear.setVisible(false);
                articleAuthorLB.setEnabled(false);
            }
        }
        else
        {
            setTitle(new Label("Article Details"));
            save.setVisible(false);
            edit.setVisible(false);
            clear.setVisible(false);
            delete.setVisible(false);
            
            articleAuthorLB.setEnabled(false);
        }
        articleFL.add(authorSearch, articleAuthorLB, titleTF, articleTA);
        // commentsFL.add(commentLB);
        
        saveCancelHL.add(save, cancel);
        addFooterComponent(saveCancelHL);
        
        setContent(outerLayoutHL);
        
        // authorLB.addValueChangeListener(event -> binder.setBean(event.getValue()));
        
        // TODO >  Event Handlers
        cancel.addClickListener(event ->
                                {
                                    this.close();
                                });
        
        delete.addClickListener(event ->
                                {
                                    new CustomNotification("Delete Clicked", "", true, 1000, Notification.Position.TOP_CENTER).open();
            
                                    String article = bean.getTitle();
            
                                    new ConfirmDialog("Delete Article",
                                                      String.format("Are you sure you want to delete %s?", article), "Yes " + BasicDialog.BeanAction.DELETE.displayText(),
                                                      (e) ->
                                                      {
                                                          bean.delete(Connection.DB_ARTICLES, bean);
                                                          System.out.printf("Deleted Article: %s\n", article);
                                                          System.out.println("Article deleted successfully");
                                                          new CustomNotification("Article deleted successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                                          e.getSource().close();
                                                      }, "Do not delete", (e) ->
                                                      {
                                                          new CustomNotification("Article not deleted", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                                          System.out.println("Article not deleted");
//                                                            message.get().getStyle().set("color", "red");
                                                          e.getSource().close();
                                                      }).open();
                                });
        clear.addClickListener(e ->
                               {
                                   //readbean manually copies object to ui fields
                                   //writebean manually copies ui data to object
                                   binder.readBean(oldBean);
            
                                   new CustomNotification("Form cleared", "info", true, 1000, Notification.Position.TOP_CENTER).open();
                               });
        save.addClickListener((e) ->
                              {
                                  Author a = articleAuthorLB.getValue();
                                  String title = titleTF.getValue();
                                  String article = articleTA.getValue();
                                  bean.setAuthor(a);
                                  bean.setAuthorID(a.getUuid());
                                  bean.setTitle(title);
                                  bean.setArticle(article);
            
                                  bean.save(bean);
            
                                  System.out.println(bean.getClass().getSimpleName() + " saved successfully");
                                  new CustomNotification(bean.getClass().getSimpleName() + " saved successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
            
                                  // execute the method (refresh data of calling interface) passed as parameter to the dialog here and if the return value is true close the dialog
                                  if (basicDialogAction.action(bean))
                                  {
                                      close();
                                  }
            
            
                              });
        edit.addClickListener((e) ->
                              {
                                  try
                                  {
                                      binder.writeBean(bean);
                                      //System.out.println("oldBean = " + oldBean);
                
                                      Article a = bean.replaceEntity(oldBean, bean);
                                      Div message;
                                      if (a.equals(bean))
                                      {
                                          /*System.out.println("author id = " + bean.getAuthorID());
                                          System.out.println("author full name = " + bean.getAuthor().getPerson().getFullName());*/
                                          System.out.println(bean.getClass().getSimpleName() + " updated successfully");
                                          new CustomNotification(bean.getClass().getSimpleName() + " updated successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                      }
                                      else
                                      {
                                          new CustomNotification(bean.getClass().getSimpleName() + " not updated", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                      }
                                      // execute the method (refresh data of calling interface) passed as parameter to the dialog here and if the return value is true close the dialog
                                      if (basicDialogAction.action(bean))
                                      {
                                          close();
                                      }
                                  }
                                  catch (ValidationException ex)
                                  {
                                      String errors = ex.getValidationErrors().stream().map(ValidationResult::getErrorMessage).collect(Collectors.joining("\n"));
                                      System.out.println(errors);
                                      getErrorDiv().setVisible(true);
                                      getErrorDiv().setText(errors);
                
                                      new CustomNotification(errors, "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                  }
                                  catch (Exception exc)
                                  {
                                      String errors = exc.getMessage();
                                      System.out.println(errors);
                                      exc.printStackTrace();
                                      getErrorDiv().setVisible(true);
                                      getErrorDiv().setText(errors);
                                      new CustomNotification(errors, "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                  }
                              });
        authorSearch.addValueChangeListener(vc ->
                                            {
                                                fdp.setFilter(vc.getValue());
                                                fdp.refreshAll();
                                            });
        articleAuthorLB.addValueChangeListener(vc ->
                                               {
                                                   if (!CustomNullChecker.nullObjectChecker(vc.getValue()))
                                                   {
                                                       //System.out.println("Item position " + articleAuthorLB.getItemPosition(vc.getValue()));
                                                       Author author = vc.getValue();
                                                       if (author.getPerson() != null)
                                                       {
                                                           String person = author.getPerson().getTitle() + " "
                                                                   + author.getPerson().getFirstName() + " "
                                                                   + author.getPerson().getLastName() + ", Email: "
                                                                   + author.getPerson().getEmailAddress() + ", Phone: "
                                                                   + author.getPerson().getPhoneNumber();
                                                           personH2.setText(person);
                                                           setTitle(titleH3, personH2);
                                                           bean.setAuthorID(author.getUuid());
                                                           //bean.setAuthor(author);
                                                       }
                
                                                   }
            
                                                   //commented notification
                                             /*Div message = new Div(new Span("Person Selected: " + person));
                                             message.setClassName("info");
                                             new CustomNotification(message).open();*/
                                               });
        
        articleAuthorLB.setRenderer(new ComponentRenderer<Emphasis, Author>(
                pc -> new Emphasis(pc.getPerson().getTitle() + " " +
                                           pc.getPerson().getFullName())));
        
        // commentLB.setRenderer(new ComponentRenderer<Emphasis, Comment>(comment -> new Emphasis(comment.getComment())));
        
        //binder.setBean(bean);
    }
    
    
}
