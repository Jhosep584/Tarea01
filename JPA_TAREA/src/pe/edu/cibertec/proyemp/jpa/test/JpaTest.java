package pe.edu.cibertec.proyemp.jpa.test;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import pe.edu.cibertec.proyemp.jpa.domain.Departamento;
import pe.edu.cibertec.proyemp.jpa.domain.Empleado;
import pe.edu.cibertec.proyemp.jpa.domain.Proyecto;

public class JpaTest {

	private EntityManager manager;

	public JpaTest(EntityManager manager) {
		this.manager = manager;
	}

	public static void main(String[] args) {

		// patron factory para obtener el EntityManager
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("myPersistenceUnit1");

		// creo el entityManager
		EntityManager em = factory.createEntityManager();

		// inyecto (DI)
		JpaTest test = new JpaTest(em);

		// Obtengo el objeto de transaccion
		EntityTransaction tx = em.getTransaction();

		// defino ambito de transaccion
		tx.begin();

		// insert, update y delete
		test.crearEmpleados();

		tx.commit();

		test.listarEmpleados();

		test.obtenerEmpleadoPorId(new Long(1));

		test.obtenerEmpleadosPorNombreDepartamento("Lima");

		test.crearProyetosDesdeUnEmpleado();
	}

	private void crearProyetosDesdeUnEmpleado() {
      Empleado emp1 = manager.find(Empleado.class, new Long(1));
		
		Proyecto proy1 = new Proyecto("Market Place");
		Proyecto proy2 = new Proyecto("e-commerce");
		
		List<Proyecto> proyectos = Arrays.asList(proy1,proy2);
		
		emp1.setProyectos(proyectos);
		manager.persist(emp1);

		
	}

	private void obtenerEmpleadosPorNombreDepartamento(String nombre) {

		
		List<Empleado> empleados = manager.createQuery(
				"select e from Empleado e where e.departamento.nombre = ?", 
				Empleado.class)
				.setParameter(1, nombre)
				.getResultList();

		for (Empleado empleado : empleados) {
			System.out.println(empleado);
		}

	}

	private void obtenerEmpleadoPorId(Long id) {

		System.out.println("--- obtener empleado x id --- ");

		// from Empleado where id = ?

		// 1ra forma
		// Empleado empleado = manager.createQuery(
		// "select e from Empleado e where e.id = :myId ",
		// Empleado.class)
		// .setParameter("myId", id)
		// .getSingleResult();

		// 2da forma (solo funciona si el parametro es el id)
		Empleado empleado = manager.find(Empleado.class, id);

		System.out.println(empleado);

	}

	private void listarEmpleados() {

		String jql = "select e from Empleado e";
		String jql2 = "from Empleado";

		String sql = "SELECT * FROM EMPLEADO";

		List<Empleado> empleados = manager.createQuery(jql2, Empleado.class).getResultList();

		for (Empleado empleado : empleados) {
			System.out.println(empleado);
		}

	}

	private void crearEmpleados() {

		Departamento lima = new Departamento("Lima");
		manager.persist(lima);

		Departamento aqp = new Departamento("Arequipa");
		manager.persist(aqp);

		Empleado emp1 = new Empleado("Carlos", lima);
		Empleado emp2 = new Empleado("Maria", lima);
		Empleado emp3 = new Empleado("Juan", aqp);
		Empleado emp4 = new Empleado("Marco", aqp);

		manager.persist(emp1);
		manager.persist(emp2);
		manager.persist(emp3);
		manager.persist(emp4);

	}

	public EntityManager getManager() {
		return manager;
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

}
