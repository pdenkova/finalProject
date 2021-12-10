package uni.fin.views.users;

import java.util.Collection;
import java.util.List;

import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.grid.Grid;

import uni.fin.data.entity.CommentEntity;
import uni.fin.data.entity.RoleEntity;

public class RoleView  extends Grid<RoleEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<RoleEntity> roles;
	
	public RoleView(final List<RoleEntity> roles) {
		super(RoleEntity.class, false);
		this.roles = roles;
		setUp();
	}

	private void setUp() {
		this.setItems(roles);
		
		final Column<RoleEntity> codeColumn = addColumn(RoleEntity::getCode);
		codeColumn.setSortable(true);
		codeColumn.setHeader("User Role");
		final Column<RoleEntity> descColumn = addColumn(RoleEntity::getDescription);
		descColumn.setSortable(true);
		descColumn.setHeader("Role Description");
		
	}
	
	public void setRoles(final Collection<RoleEntity>roles) {
		this.roles.clear();
		this.roles.addAll(roles);
		this.setItems(roles);
	}

}
