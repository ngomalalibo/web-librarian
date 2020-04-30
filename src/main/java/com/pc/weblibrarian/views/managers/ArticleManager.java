package com.pc.weblibrarian.views.managers;

import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.CustomSearchTextField;
import com.pc.weblibrarian.customcomponents.Fragment;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.ArticleDP;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.entity.Article;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.entity.Person;
import com.pc.weblibrarian.security.SecuredByRole;
import com.pc.weblibrarian.views.BasicDialog;
import com.pc.weblibrarian.views.MainFrame;
import com.pc.weblibrarian.views.dialogs.ArticleCRUDDialog;
import com.vaadin.flow.component.Html;
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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

@SecuredByRole(value = {"USER"})
@Route(value = "articles", layout = MainFrame.class)
@PreserveOnRefresh
public class ArticleManager extends Fragment
{
    private ArticleDP dp = new ArticleDP();
    private ConfigurableFilterDataProvider<Article, Void, String> configurableFilterDataProvider = dp.withConfigurableFilter();
    
    private Grid<Article> grid = new Grid<>(Article.class, false);
    private static final boolean superadmin = true;
    
    private int total;
    
    public ArticleManager()
    {
        super();
        total = configurableFilterDataProvider.size(new Query<>());
        //System.out.println("Total "+total);
        
        grid.setDataProvider(configurableFilterDataProvider);
        grid.setMultiSort(true);
        grid.setColumnReorderingAllowed(true);
        
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        GridSingleSelectionModel<Article> singleSelect = (GridSingleSelectionModel<Article>) grid.getSelectionModel();
        singleSelect.asSingleSelect();
        singleSelect.setDeselectAllowed(true);
        
        /*grid.addItemClickListener(d ->
                                  {
                                      Article p = d.getItem();
            
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
                                  });*/
        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(pc ->
                                        {
                                            VerticalLayout layout = new VerticalLayout();
                                            H3 title = new H3(pc.getTitle());
                                            Paragraph p = new Paragraph(pc.getArticle());
                    
                                            layout.add(title, p);
                                            return layout;
                                        }));
        
        Grid.Column<Article> title = grid.addColumn(Article::getTitle).setHeader("Title").setResizable(true);
        Grid.Column<Article> articleColumn = grid.addColumn(TemplateRenderer.<Article>of("[[item.article]]....")
                                                                    .withProperty("article", a -> a.getArticle().substring(0, a.getArticle().length() % 25)), "article").setHeader("Article");
        grid.addColumn(TemplateRenderer.<Article>of("[[item.author]]")
                               .withProperty("author", d ->
                               {
                                   Author author = GetObjectByID.getObjectById(d.getAuthorID(), Connection.authors);
                                   Person person = GetObjectByID.getObjectById(author.getPersonId(), Connection.persons);
                                   author.setPerson(person);
                                   return d.getAuthor().getPerson().getFullName();
                               })).setHeader("Author").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(a ->
                                               {
                                                   NumberField rating = new NumberField();
                                                   rating.setValue(1d);
                                                   rating.setHasControls(true);
                                                   rating.setMin(1);
                                                   rating.setMax(5);
                                                   rating.setEnabled(false);
                                                   return rating;
                                               })).setHeader(new Html("<b> Rating </b>"));
        grid.addComponentColumn(article ->
                                {
                                    Div fl = new Div();
                                    fl.getStyle().set("textAlign", "right");
            
                                    Button edit = new Button(new IronIcon("lumo", "edit"));
                                    edit.getElement().setAttribute("theme", "small icon");
                                    edit.getStyle().set("marginRight", "0.4em");
                                    edit.addClickListener(click ->
                                                          {
                                                              /*System.out.println("author id = " + article.getAuthorID());
                                                              System.out.println("author full name = " + article.getAuthor().getPerson().getFullName());*/
                                                              new ArticleCRUDDialog(article,
                                                                                    superadmin ? BasicDialog.BeanAction.EDIT : BasicDialog.BeanAction.VIEW,
                                                                                    (a) ->
                                                                                    {
                                                                                        grid.getDataProvider().refreshItem(article);
                                                                                        grid.getDataProvider().refreshAll();
                                                                                        return true;
                                                                                    }, BasicDialog.DialogSize.MID).open();
                                                          });
                                    Button delete = new Button(new IronIcon("lumo", "cross"));
                                    delete.getElement().setAttribute("theme", "small icon error");
                                    delete.addClickListener(click ->
                                                            {
                                                                new CustomNotification("Delete clicked", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                                                new ConfirmDialog("Delete Article",
                                                                                  String.format("Are you sure you want to delete %s?", article.getTitle()), "Yes " + BasicDialog.BeanAction.DELETE.displayText(),
                                                                                  (e) ->
                                                                                  {
                                                                                      article.delete(Connection.DB_ARTICLES, article);
                    
                                                                                      System.out.printf("Deleted Article: %s\n", article.getTitle());
                                                                                      System.out.println("Article deleted successfully");
                                                                                      new CustomNotification("Article deleted successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                                                                      grid.getDataProvider().refreshItem(article);
                                                                                      grid.getDataProvider().refreshAll();
                    
                                                                                      e.getSource().close();
                                                                                  }, "Do not delete", (e) ->
                                                                                  {
                                                                                      System.out.println("Article not deleted");
                                                                                      new CustomNotification("Article not deleted", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
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
        topRow.join(title, articleColumn).setComponent(new Label("Article Information"));
        
        addHeaderComponent(new H1("Fragment Main Header"));
        addHeaderComponent(new H3("Articles"));
        
        SmallButton addentity = new SmallButton("New Article").theme(ButtonVariant.LUMO_PRIMARY.getVariantName());
//        addentity.setIcon(new Icon("lumo", "add"));
        addentity.setIcon(new Icon(VaadinIcon.ADD_DOCK));
        //addentity.setVisible(MainFrame.getUserRoleType().isSuperAdmin());
        
        addentity.addClickListener(clk ->
                                   {
                                       //Article c = new Article("", "", "", Collections.singletonList(new Comment()), 0);
                                       new ArticleCRUDDialog(new Article(),
                                                             BasicDialog.BeanAction.NEW,
                                                             (a) ->
                                                             {
                                                                 //refresh data
                                                                 refreshDataTable((Article) a);
                                                                 grid.getDataProvider().refreshItem((Article) a);
                                                                 grid.getDataProvider().refreshAll();
                                                                 return true;
                                                             }, BasicDialog.DialogSize.MID).open();
                                   });
        
        
        Span totalcountspan = new Span(total + " Total");
        totalcountspan.addClassNames("secondary-text", "font-size-xs");
        configurableFilterDataProvider.addDataProviderListener(lst -> totalcountspan.setText(total + " Total"));
        
        CustomSearchTextField searchbox = new CustomSearchTextField("Search", ValueChangeMode.EAGER, new Icon("lumo", "search"), false/*not needed here with TextFieldClearButton in use*/, "Search By Title", "small");
        searchbox.addValueChangeListener(event ->
                                         {
                                             configurableFilterDataProvider.setFilter(searchbox.getValue());
                                             configurableFilterDataProvider.refreshAll();
            
                                             total = configurableFilterDataProvider.size(new Query<>());
                                             totalcountspan.setText(total + " Total");
                                         });
        appendHeaderBComponent(new HeaderControls(searchbox, addentity));
        addFooterComponent(totalcountspan);
        
        
        addToolbarComponent(new Paragraph("You can sort, edit and delete records on this grid."));
        
        
        addContent(grid);
        addFooterComponent(new H6("Fragment Footer"));
        
    }
    
    private void refreshDataTable(Article a)
    {
        configurableFilterDataProvider.refreshItem(a);
        grid.getDataProvider().refreshAll();
    }
}
