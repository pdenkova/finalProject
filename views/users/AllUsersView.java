package uni.fin.views.users;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


import uni.fin.data.entity.RoleEntity;
import uni.fin.data.entity.UserEntity;
import uni.fin.data.service.UserEnService;
import uni.fin.data.service.repo.RoleRepo;
import uni.fin.views.MainLayout;

@PageTitle("All Users")
@Route(value = "users", layout = MainLayout.class)
@RolesAllowed("admin")
public class AllUsersView extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	private UserEnService userService;
	private Grid<UserEntity> grid;
	private ListDataProvider<UserEntity> dataProvider;
	private Collection<UserEntity> users;
	private PasswordEncoder passwordEncoder;
	private RoleView roleView;
	private RoleRepo roleRepo;
	
	public AllUsersView(@Autowired final UserEnService userService, @Autowired PasswordEncoder passwordEncoder,
			@Autowired RoleRepo roleRepo) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.roleRepo = roleRepo;
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		grid = new Grid<UserEntity>(UserEntity.class, false);
		
		users = userService.findAll();
		dataProvider = new ListDataProvider<UserEntity>(users);
		grid.setDataProvider(dataProvider);
		grid.addItemDoubleClickListener(l -> createUserForm(l.getItem()));
		final Column<UserEntity> nameColumn = grid.addColumn(UserEntity::getUsername).setHeader("Name").setSortable(true);		
		final Column<UserEntity> cityColumn = grid.addColumn(UserEntity::getCity).setHeader("City").setSortable(true);
		final Column<UserEntity> emailColumn = grid.addColumn(UserEntity::getEmail).setHeader("Email").setSortable(true);
		final HeaderRow headerRow = grid.appendHeaderRow();

		final TextField nameFilterField = new TextField();
		nameFilterField.addValueChangeListener(l -> {
			final String valueName = l.getValue();
			final SerializablePredicate<UserEntity> filter = user -> 
										StringUtils.containsIgnoreCase(user.getUsername(), valueName);
			addFilter(filter);
		});
		nameFilterField.setWidthFull();
		headerRow.getCell(nameColumn).setComponent(nameFilterField);
		
		final TextField cityFilterField = new TextField();
		cityFilterField.addValueChangeListener(l -> {
			final String valueCity = l.getValue();
			final SerializablePredicate<UserEntity> filter = user ->
										StringUtils.containsIgnoreCase(user.getCity(), valueCity);
			addFilter(filter);
		});
		cityFilterField.setWidthFull();
		headerRow.getCell(cityColumn).setComponent(cityFilterField);
		
		final TextField emailFilterField = new TextField();
		emailFilterField.addValueChangeListener(l -> {
			final String valueEmail = l.getValue();
			final SerializablePredicate<UserEntity> filter = user ->
										StringUtils.containsIgnoreCase(user.getEmail(), valueEmail);
			addFilter(filter);
		});
		emailFilterField.setWidthFull();
		headerRow.getCell(emailColumn).setComponent(emailFilterField);
		
		final RoleView roleView = createRoleView();
		final HorizontalLayout roleButtons = createRoleButtons();
		add(createButtons(), grid, roleButtons, roleView);
		
	}
	
	private HorizontalLayout createButtons() {
		
		final Button addButton = new Button("Add", l -> {
			final UserEntity newUser = new UserEntity();
			createUserForm(newUser);
		});
		
		addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		final Button editButton = new Button("Edit", l -> 
		 	createUserForm(grid.asSingleSelect().getValue())
		);
		editButton.setEnabled(false);
		editButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		
		
		final Button removeButton = new Button("Remove", l -> {
			final Dialog dialog = new Dialog();
			final H1 title = new H1("Are you sure you want to remove this user?");
			final Button okay = new Button("OK", li1 -> {
				userService.delete(grid.asSingleSelect().getValue().getId());
				resetGrid();
				dialog.close();
				Notification notification = Notification.show("User has been successfully removed!");
				notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			});
			final Button cancel = new Button("Cancel", li2 -> dialog.close());
			final HorizontalLayout dialogButtons = new HorizontalLayout(okay, cancel);
			final VerticalLayout dialogBody = new VerticalLayout(title, dialogButtons);
			dialog.add(dialogBody);
			dialog.open();
			
		
		});
		removeButton.setEnabled(false);
		removeButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		
		final SingleSelect<Grid<UserEntity>, UserEntity> asSingleSelect = grid.asSingleSelect();
		asSingleSelect.addValueChangeListener(l -> {
			final UserEntity value = l.getValue();
			removeButton.setEnabled(value != null);
			editButton.setEnabled(value != null);
		});
		
		final HorizontalLayout buttons = new HorizontalLayout(addButton, editButton, removeButton);
	
		return buttons;
	}

	@SuppressWarnings("deprecation")
	private void createUserForm(final UserEntity newUser) {
		final Dialog dialog = new Dialog();
		final TextField username = new TextField();
		final PasswordField password = new PasswordField();
		final PasswordField passwordRepeat = new PasswordField();
		final EmailField email = new EmailField();
		final TextField city = new TextField();
		final TextField avatarLocation = new TextField();
		
		final ComboBox<UserEntity> userCombo = new ComboBox<UserEntity>();
		userCombo.setDataProvider(userService::fetchItems, userService::count);
		userCombo.setItemLabelGenerator(UserEntity::getUsername);
		
		final BeanValidationBinder<UserEntity> binder = new BeanValidationBinder<>(UserEntity.class);
		
		binder.bind(username, UserEntity::getUsername, UserEntity::setUsername);
		binder.bind(password, UserEntity::getPassword, (u, value) -> u.setPassword(passwordEncoder.encode(value)));
		binder.forField(email).withValidator(new EmailValidator("Insert a valid email"))
											.bind(UserEntity::getEmail, UserEntity::setEmail);
		binder.bind(city, UserEntity::getCity, UserEntity::setCity);
		binder.bind(avatarLocation, UserEntity::getAvatarLocation, UserEntity::setAvatarLocation);
		binder.readBean(newUser);
		
		final FormLayout formLayout = new FormLayout();
		formLayout.addFormItem(username, "Username");
		formLayout.addFormItem(password, "Password");
		formLayout.addFormItem(passwordRepeat, "Repeat password");
		formLayout.addFormItem(email, "Email");
		formLayout.addFormItem(city, "City");
		formLayout.addFormItem(avatarLocation, "Avatar Location");
		formLayout.addFormItem(userCombo, "Users");
		
		
		final Button okay = new Button("OK", li1 -> {
			boolean beanIsValid = binder.writeBeanIfValid(newUser);
			if(beanIsValid) {
				userService.update(newUser);
				resetGrid();
				dialog.close();
				Notification notification = Notification.show("New user added successfully!");
				notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			}
			
		});
		final Button cancel = new Button("Cancel", li2 -> dialog.close());
		final HorizontalLayout dialogButtons = new HorizontalLayout(okay, cancel);
		final VerticalLayout dialogBody = new VerticalLayout(formLayout, dialogButtons);
		dialogBody.expand(formLayout);
		dialogBody.setSizeFull();
		dialog.setWidth("300px");
		dialog.setHeight("450px");
		
		dialog.add(dialogBody);
		dialog.open();
	}

	
	private RoleView createRoleView() {
		roleView = new RoleView(new ArrayList<>());
		grid.asSingleSelect().addValueChangeListener(l -> {
			final UserEntity value = l.getValue();
			if(null != value) {
				roleView.setRoles(value.getRoles());
			}else {
				roleView.setRoles(new ArrayList<>());
			}
		});
		return roleView;
	}
	
	private HorizontalLayout createRoleButtons() {
		
		final Button addRole = new Button("Add role", l-> {
			final Dialog dialog = new Dialog();
			dialog.setWidth("500px");
			final RoleView chooseRole = new RoleView(roleRepo.findAll());
			final UserEntity user = grid.asSingleSelect().getValue();
			final Button ok = new Button("OK", li -> {
				final RoleEntity newRole = chooseRole.asSingleSelect().getValue();
				if(newRole != null) {
					
					user.addRole(newRole);
					userService.update(user);
					resetGrid();
					dialog.close();
			}
			});
			final Button close = new Button("Close", li -> dialog.close());
			final HorizontalLayout dialogButtons = new HorizontalLayout(ok, close);
			final VerticalLayout dialogBody = new VerticalLayout(chooseRole, dialogButtons);
			dialog.add(dialogBody);
			dialog.open();
			
		});
		addRole.addThemeVariants(ButtonVariant.LUMO_SMALL);
		addRole.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		final Button removeRole = new Button("Remove role");
		removeRole.addThemeVariants(ButtonVariant.LUMO_SMALL);
		removeRole.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		removeRole.setEnabled(false);
		removeRole.addClickListener(l -> {
			final RoleEntity selectedRole = roleView.asSingleSelect().getValue();
			final UserEntity selectedUser = grid.asSingleSelect().getValue();
			selectedUser.removeRole(selectedRole);
			userService.update(selectedUser);
			
			resetGrid();
		});
		roleView.asSingleSelect().addValueChangeListener(l ->{
			removeRole.setEnabled(l.getValue() != null);
		});
		final HorizontalLayout roleButtons = new HorizontalLayout (addRole, removeRole);
		return roleButtons;
	}
	
	private void resetGrid() {
		grid.select(null);
		dataProvider.clearFilters();
		users.clear();
		users.addAll(userService.findAll());
		dataProvider.refreshAll(); 
	}

	private void addFilter(final SerializablePredicate<UserEntity> filter) {
		dataProvider.clearFilters();
		dataProvider.addFilter(filter);
	}
	
}
