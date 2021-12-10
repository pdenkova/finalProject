package uni.fin.views.comments;


import java.util.Collection;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;

import uni.fin.data.entity.CommentEntity;
import uni.fin.data.service.CommentService;
import uni.fin.views.MainLayout;

@PageTitle("Comments")
@Route(value = "comment-list", layout = MainLayout.class)
@RolesAllowed("user")
public class CommentList extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CommentService commentService;
	private Grid<CommentEntity> grid;
	private ListDataProvider<CommentEntity>dataProvider;
	private Collection<CommentEntity> comments;
	
	public CommentList(@Autowired final CommentService commentService) {
		this.commentService = commentService;
		init();
	}
	@SuppressWarnings("deprecation")
	private void init() {

		grid = new Grid<CommentEntity>(CommentEntity.class, false);
		comments = commentService.findAll();
		dataProvider = new ListDataProvider<CommentEntity>(comments);
		grid.setDataProvider(dataProvider);
	
		final Column<CommentEntity>commentColumn = grid.addColumn(CommentEntity::getComment);
		commentColumn.setHeader("Comments");
		
		add(createButtons(), grid);
 }

	private void createNewComment(final CommentEntity newComment) {
		
		final Dialog dialog = new Dialog();
		final TextArea comment = new TextArea();
		
		final BeanValidationBinder<CommentEntity> binder = new BeanValidationBinder<>(CommentEntity.class);
		binder.forField(comment).withValidator(com -> com.length() <=500, "No more than 500 characters")
		 									.bind(CommentEntity::getComment, CommentEntity::setComment);
		binder.readBean(newComment);
		
		final FormLayout commentForm = new FormLayout();
		commentForm.addFormItem(comment, "Comment");
		commentForm.getMaxWidth();
		
		final Button post = new Button("Post", l -> {
			boolean beanIsValid = binder.writeBeanIfValid(newComment);
			if(beanIsValid) {
				commentService.update(newComment);
				resetGrid();
				dialog.close();
			}
			});
		final Button cancel = new Button("Cancel", li -> dialog.close());
		final HorizontalLayout dialogButtons = new HorizontalLayout(post, cancel);
		final VerticalLayout dialogBody = new VerticalLayout(commentForm, dialogButtons);
		dialogBody.expand(commentForm);
		dialogBody.setSizeFull();
		dialog.setWidth("400px");
		dialog.setHeight("400px");
		dialog.add(dialogBody);
		dialog.open();
	}
	private HorizontalLayout createButtons() {
		
		Button addButton = new Button(new Icon(VaadinIcon.PLUS), l -> {
			final CommentEntity newComment = new CommentEntity();
			createNewComment(newComment);
		});
		
		addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	
		Button editButton = new Button(new Icon(VaadinIcon.EDIT), l -> {
			createNewComment(grid.asSingleSelect().getValue());
		});
		editButton.setEnabled(false);
		editButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		
		Button delButton = new Button(new Icon(VaadinIcon.CLOSE), l -> {
			final Dialog dialog = new Dialog();
			final H1 title = new H1 ("Are you sure you want to remove this comment?");
			final Button okay = new Button("OK", li -> {
				commentService.delete(grid.asSingleSelect().getValue().getId());
				resetGrid();
				dialog.close();
			});
			final Button cancel = new Button("Cancel", lis -> dialog.close());
			final HorizontalLayout dialogButtons = new HorizontalLayout(okay, cancel);
			final VerticalLayout dialogBody = new VerticalLayout(title, dialogButtons);
			dialog.add(dialogBody);
			dialog.open();
		});
		delButton.setEnabled(false);
		delButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

		final SingleSelect<Grid<CommentEntity>, CommentEntity> asSingleSelect = grid.asSingleSelect();
		asSingleSelect.addValueChangeListener(l -> {
			final CommentEntity value = l.getValue();
			editButton.setEnabled(value != null);
			delButton.setEnabled(value != null);
		});
		
		final HorizontalLayout buttons = new HorizontalLayout(addButton, editButton, delButton);
	
	    return buttons;
	}
	private void resetGrid() {
		comments.clear();
		comments.addAll(commentService.findAll());
		dataProvider.refreshAll();
	}

	}
