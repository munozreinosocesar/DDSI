//////////////////////////////////////////////////////////////////
//                                                              //
//             SISTEMA DE INFORMACION - ZAPATERIA               //
//                                                              //
//                                                              //
//                                                              //
//                   DAVID ARMENTEROS SOTO                      //
//		     IÑAKI MELGUIZO MARCOS                      //
//                   CESAR MUÑOZ REINOSO                        //
//                                                              //
//                                                              //
//                                         2020-2021            //
//////////////////////////////////////////////////////////////////

package DAO;



import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;


public class ZapateriaDAO {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;
	
	public ZapateriaDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
		
	public void connect() throws SQLException {
		if (jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(
										jdbcURL, jdbcUsername, jdbcPassword);
			jdbcConnection.setAutoCommit(false);
			System.out.println("Connected to the database");
		}
	}
	
	public void disconnect() throws SQLException {
		if (jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
			System.out.println("Disconnected to the database");

		}
	}

	public void commit() throws SQLException {
		String sql;
		
		sql = "COMMIT";
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.executeUpdate();
		statement.close();
	}
		
	//SUBSISTEMA CLIENTES
	public void registrarCliente(String nombre, String correo, int tlf) throws SQLException {
		
		int idCliente = -1;
		
		String sql = "SELECT MAX (idCliente) FROM Cliente";
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);

		ResultSet resultSet = statement.executeQuery();
		if(resultSet.next()) {
			idCliente = resultSet.getInt(1) + 1;
		}
				
		statement.close();


		sql = "INSERT INTO Cliente (idCliente,Nombre,Correo,Telefono) VALUES (?,?,?,?)";
				
		try {

		 statement = jdbcConnection.prepareStatement(sql);
			statement.setInt(1, idCliente);
			statement.setString(2, nombre);
			statement.setString(3, correo);
			statement.setInt(4, tlf);
			
			statement.executeUpdate();

			statement.close();

	    } catch (Exception e) {
	        System.out.println("Insercion del cliente erronea. " + e.getMessage());
	    }	
		
	}
	
	
	public void modificarCliente(int id, String correo) throws SQLException {
		if(!estaEliminado(id)) {
		String sql = "UPDATE Cliente set Correo=? where idCliente=? ";
		
		try {

			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, correo);
			statement.setInt(2, id);
			
			statement.executeUpdate();
			statement.close();

		    } catch (Exception e) {
		        System.out.println("Modificacion del cliente erronea. " + e.getMessage());
		    }	
		}else {
	        System.out.println("El cliente está eliminado. ");
		}
		
	}
	
	

	
	public void mostrarCliente(String correo) throws SQLException {
		
		
		
		
		String sql = "SELECT idCliente,Nombre,Telefono from Cliente where correo=?";
		
		try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, correo );
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				int idCliente = resultSet.getInt("idCliente");
				String Nombre = resultSet.getString("Nombre");
				String Telefono = resultSet.getString("Telefono");
				

				if(!estaEliminado(idCliente)) {
					
					System.out.print(idCliente + "  " + Nombre + "  " +  Telefono + "\n");
	
				}else {
			        System.out.println("El cliente está eliminado. ");
				}
					
			}
			else {
				System.out.print("No existe ningun cliente con el correo " + correo + ". \n");
 
			}
			resultSet.close();
			statement.close();
						
		    } catch (Exception e) {
		        System.out.println("Mostrado del cliente erroneo. " + e.getMessage());
		    }	
	}
	
	public void eliminarCliente(int id) throws SQLException {
		
		String sql = "INSERT INTO Eliminado (idCliente) VALUES (?)";
		
		try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setInt(1, id);
		
			statement.executeUpdate();		
			statement.close();
						
		    } catch (Exception e) {
		        System.out.println("Eliminado del cliente erroneo. " + e.getMessage());
		    }	
		

	}
	
	
	//SUBSISTEMA VENTAS
	
	public void ventaDirecta(int idCli, int idpro, int cant) throws SQLException {
		
		if(!estaEliminado(idCli)) {
			
			int idVenta = -1;
			
			String sql = "SELECT MAX (idVenta) FROM Venta";
			
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);

			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()) {
				idVenta = resultSet.getInt(1) + 1;
			}
					
			statement.close();
			
			sql = "INSERT INTO Venta (idVenta,idCliente,idProducto,Cantidad) VALUES (?,?,?,?)";
			
			try {
				
				statement = jdbcConnection.prepareStatement(sql);
				statement.setInt(1, idVenta);
				statement.setInt(2, idCli);
				statement.setInt(3,idpro);
				statement.setInt(4,cant);
				
				statement.executeUpdate();		
				statement.close();		
							
			    } catch (Exception e) {
			        System.out.println("Insercion de venta erronea. " + e.getMessage());
			    }	
		}else {
	        System.out.println("El cliente está eliminado. ");
		}
			
	}
	
	
	
	
	public void ventasCliente(int id) throws SQLException {
		if(!estaEliminado(id)) {
			int ventas = 0;
			String sql = "SELECT * FROM Venta WHERE idCliente = ?";
					
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setInt(1, id);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				if (ventas == 0) {
					System.out.print("idVenta  idCliente  idProducto  Cantidad\n");
				}
				ventas++;
				int idVenta = resultSet.getInt("idVenta");
				int idCliente = resultSet.getInt("idCliente");
				int idProducto = resultSet.getInt("idProducto");
				int Cantidad = resultSet.getInt("Cantidad");
				
				System.out.print(idVenta + "           " + idCliente + "              " +  idProducto + "           " + Cantidad + "\n");
			}	
			if (ventas == 0) {
				System.out.print("El cliente no ha realizado ninguna venta. \n");
			}
				
			resultSet.close();
			statement.close();
			
		}else {
	        System.out.println("El cliente está eliminado. ");
		}
		
	}

	public void realizarReserva(int idCli, int idpro, int cant) throws SQLException {
		if(!estaEliminado(idCli)) {

		int idReserva = -1;
		
		String sql = "SELECT MAX (idReserva) FROM Reserva";
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);

		ResultSet resultSet = statement.executeQuery();
		if(resultSet.next()) {
			idReserva = resultSet.getInt(1) + 1;
		}
				
		statement.close();
		
		 sql = "INSERT INTO Reserva (idReserva,idCliente,idProducto,Cantidad) VALUES (?,?,?,?)";
		
			try {
				statement = jdbcConnection.prepareStatement(sql);
				statement.setInt(1, idReserva);
				statement.setInt(2, idCli);
				statement.setInt(3, idpro);
				statement.setInt(4, cant);
				
				statement.executeUpdate();		
				statement.close();
								
			    } catch (Exception e) {
			        System.out.println("Inserción de Reserva erronea. " + e.getMessage());
			    }
		}else {
	        System.out.println("El cliente está eliminado. ");
		}
	}

	public void ventaReserva(int id) throws SQLException {
		
		String sql = "SELECT * FROM Cancelada WHERE idReserva=?";
		boolean cancelada = false;
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, id);
				
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next()) {
			cancelada = true;
		}
		
		statement.close();
		if(!cancelada) {

			String sql2 = "INSERT INTO Completada (idReserva) VALUES (?)";
			try {
				
				PreparedStatement statement2 = jdbcConnection.prepareStatement(sql2);
				statement2.setInt(1, id);
				
				statement2.executeUpdate() ;

				statement2.close();
				
								
			    } catch (Exception e) {
			        System.out.println("Completación de reserva erronea. " + e.getMessage());
			    }

		
		}else {
	        System.out.println("La reserva ha sido cancelada. ");
		}

	}
	
	public void cancelarReserva(int id) throws SQLException {
		String sql = "SELECT * FROM Completada WHERE idReserva=?";
		boolean completada = false;
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, id);
				
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next()) {
			completada = true;
		}
		
		statement.close();
		
		if(!completada) {
			String sql2 = "INSERT INTO Cancelada (idReserva) VALUES (?)";
		
			try {

				PreparedStatement statement2 = jdbcConnection.prepareStatement(sql2);
				statement2.setInt(1, id);
				
				statement2.executeUpdate() ;

				statement2.close();
				
								
			    } catch (Exception e) {
			        System.out.println("Eliminacion de reserva erronea. " + e.getMessage());
			    }
		}else {
	        System.out.println("La reserva ya ha sido completada. ");
		}
	}

	
	//SUBSISTEMA PEDIDOS
	
	public void realizarPedido(int idpro, int cant) throws SQLException {
		
		int idPedido = -1;
		
		String sql = "SELECT MAX (idPedido) FROM Pedido";
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);

		ResultSet resultSet = statement.executeQuery();
		if(resultSet.next()) {
			idPedido = resultSet.getInt(1) + 1;
		}
				
		statement.close();
		
		sql = "INSERT INTO Pedido (idPedido,idProducto,Cantidad) VALUES (?,?,?)";
	 
		try {
			 statement = jdbcConnection.prepareStatement(sql);
				statement.setInt(1, idPedido);
				statement.setInt(2, idpro);
				statement.setInt(3, cant);

				statement.executeUpdate() ;
				statement.close();

			
							
		    } catch (Exception e) {
		        System.out.println("Insercion de pedido erroneo. " + e.getMessage());
		    }

				
	}
	

	public void confirmarPedido(int idped) throws SQLException {
		
		int idProducto, Cantidad, stock, stock_nuevo;
		
		LocalDate todayLocalDate = LocalDate.now( ZoneId.of( "America/Montreal" ) );
		java.sql.Date sqlDate = java.sql.Date.valueOf( todayLocalDate );
		
		String sql = "SELECT * FROM Devuelto WHERE idPedido=?";
		boolean devuelto = false;
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, idped);
				
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next()) {
			devuelto = true;
		}
		
		statement.close();
		
		 sql = "SELECT * FROM Recibido WHERE idPedido=?";
		boolean recibido = false;
		
		 statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, idped);
				
		 resultSet = statement.executeQuery();
		
		if(resultSet.next()) {
			recibido = true;
		}
		
		statement.close();
		
		
		
		if(!devuelto && !recibido) {
			String sql2 = "INSERT INTO Recibido (idPedido) VALUES (?)";
			try {
						
			PreparedStatement statement2 = jdbcConnection.prepareStatement(sql2);
			statement2.setInt(1, idped);
		
		 	statement2.executeUpdate();

		 	statement2.close();
		
		 	
		 	
		 	String sql3 = "SELECT * FROM Pedido WHERE idPedido=?";

		 	PreparedStatement statement3 = jdbcConnection.prepareStatement(sql3);
		 	statement3.setInt(1, idped);
		
		 	ResultSet resultSet3 = statement3.executeQuery();

		 	if(resultSet3.next()) {
		 		idProducto = resultSet3.getInt("idProducto");
		 		Cantidad = resultSet3.getInt("Cantidad");
		 	
		 		String sql4 = "SELECT * FROM Producto WHERE idProducto=?";

		 		PreparedStatement statement4 = jdbcConnection.prepareStatement(sql4);
		 		statement4.setInt(1, idProducto);
			
				ResultSet resultSet4 = statement4.executeQuery();

		 		if(resultSet4.next()) {
		 			stock = resultSet4.getInt("Stock");

		 			stock_nuevo= Cantidad + stock;
			
		 			String sql5 = "UPDATE Producto set Fecha=?, Stock=? where idProducto=?";

		 			PreparedStatement statement5 = jdbcConnection.prepareStatement(sql5);
		 			statement5.setDate(1, sqlDate);
		 			statement5.setInt(2, stock_nuevo);
		 			statement5.setInt(3, idProducto);
		 			
				 	statement5.executeUpdate();

		 			statement5.close();
			
		 		}
	 			statement4.close();
		 	}
 			statement3.close();

		
	    } catch (Exception e) {
	        System.out.println("Confirmacion de pedido erroneo. " + e.getMessage());
	    }
		}else {
			System.out.print("El pedido introducido no se puede confirmar. \n");	
		}
	}

	
	
	public void verPedido(int idped) throws SQLException {
		String sql = " SELECT * from Devuelto where idPedido=? ";
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, idped);

		ResultSet resultSet = statement.executeQuery();
		String estado = null;
		
		if(resultSet.next()) {
			estado="Devuelto";
		}
		
		resultSet.close();
		statement.close();
		
		if(estado!="Devuelto") {
			sql = " SELECT * from Recibido where idPedido=? ";
			
			statement = jdbcConnection.prepareStatement(sql);
			statement.setInt(1, idped);

			resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				estado="Recibido";
			}
			
			resultSet.close();
			statement.close();
			
		}
		
		if(estado!="Devuelto" && estado!="Recibido" ) {
			sql = " SELECT * from Pedido where idPedido=? ";
			
			statement = jdbcConnection.prepareStatement(sql);
			statement.setInt(1, idped);

			resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				estado="En transito";
			}
			
			resultSet.close();
			statement.close();
		}
		
		if(estado != null) {
			System.out.print("El estado del paquete es : " + estado + "\n");	
		}else {
			System.out.print("El id introducido no corresponde a ningun pedido. \n");	
		}
	}
	
	
	public void devolverPedido(int idped) throws SQLException { 
		
		String sql = "INSERT INTO Devuelto (idPedido) VALUES (?)";
		try {
			
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setInt(1, idped);
			
			statement.executeUpdate() ;
			statement.close();
				
		    } catch (Exception e) {
		        System.out.println("El id introducido no corresponde a ningun pedido. \n" + e.getMessage());
		    }

		
	}
	
	
	//SUBSISTEMA MARCAS/PROMOCIONES
	
	public void promocionProducto(int idpro, int idprov, int cant, float precio, int tiempoexp) throws SQLException {
		
		LocalDate todayLocalDate = LocalDate.now( ZoneId.of( "America/Montreal" ) );
	    java.sql.Date sqlDate = java.sql.Date.valueOf( todayLocalDate );
	    
	    		
		String sql = "INSERT INTO Producto (idProducto, Fecha, Stock, idProveedor) VALUES (?,?,?,?)";
				
		try {

			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setInt(1, idpro);
			statement.setDate(2, sqlDate);
			statement.setInt(3, 0);
			statement.setInt(4, idprov);
			statement.executeUpdate();

			statement.close();
			
			String sql2 = "INSERT INTO Promocion (Cantidad, Precio,TiempoExp,idProducto) VALUES (?,?,?,?)";
			
			try {

				PreparedStatement statement2 = jdbcConnection.prepareStatement(sql2);
				statement2.setInt(1, cant);
				statement2.setFloat(2, precio);
				statement2.setInt(3, tiempoexp);
				statement2.setInt(4, idpro);
				
				statement2.executeUpdate();
				statement2.close();

		    } catch (Exception e) {
		        System.out.println("Insercion de la promocion erronea. " + e.getMessage());
		    }
	    } catch (Exception e) {
	        System.out.println("Insercion del nuevo producto erronea.. " + e.getMessage());
	    }
		
				
	}
	
	public void liquidacionProducto(int numMeses, int descuento) throws SQLException {
		
		LocalDate todayLocalDate = LocalDate.now( ZoneId.of( "America/Montreal" ) );
	    Period diferencia;
	    
		int idliq=-1;
	    
		String sql = "SELECT MAX (idLiquidacion) FROM Liquidacion";
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);

		ResultSet resultSet = statement.executeQuery();
		if(resultSet.next()) {
			idliq = resultSet.getInt(1) + 1;
		}
				
		statement.close();
		resultSet.close();

	    
		String sql2 = "INSERT INTO Liquidacion (idLiquidacion, NumMeses, Descuento) VALUES(?,?,?)";
		PreparedStatement statement2 = jdbcConnection.prepareStatement(sql2);
		statement2.setInt(1,idliq);
		statement2.setInt(2,numMeses);
		statement2.setFloat(3, descuento );

		
		statement2.executeUpdate();
		statement2.close();	
	    
	    
		String sql3 = "Select * from Producto";
		
		PreparedStatement statement3 = jdbcConnection.prepareStatement(sql3);
		ResultSet resultSet3 = statement3.executeQuery();
		
		while(resultSet3.next()) {
			Date fecha = resultSet3.getDate("Fecha");
			diferencia = Period.between(fecha.toLocalDate(), todayLocalDate);

			if((diferencia.getMonths() + diferencia.getYears() * 12) > numMeses) {
				int id = resultSet3.getInt("idProducto");
				float precioini = resultSet3.getFloat("Precio");
				float preciofin = precioini  * ((float)1.0 - (float)((float)descuento/100.0));
				
				
				String sql4 = "INSERT INTO Amortiza (idLiquidacion, idProducto, Precio) VALUES(?,?,?)";
				try {

					PreparedStatement statement4 = jdbcConnection.prepareStatement(sql4);
					statement4.setInt(1,idliq);
					statement4.setInt(2,id);
					statement4.setFloat(3, preciofin );

					statement4.executeUpdate();
					statement4.close();		

			    } catch (Exception e) {
			        System.out.println("El producto con id " + id + " ya está liquidado. ");
			    }			
				
		
			}

				
		}	
		
		statement3.close();
		resultSet3.close();
			
		
	}
	

	public void calificarProveedor(int idprov) throws SQLException { 
		
		int cuantos=0, cuantos2=0, idpro;
		
		String sql = "SELECT idProducto FROM Producto WHERE idProveedor = ? ";

		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		
		statement.setInt(1, idprov);

		ResultSet resultSet = statement.executeQuery();
		
		while(resultSet.next()) {
			idpro = resultSet.getInt("idProducto");
			
			String sql2 = "SELECT COUNT (*) FROM Venta WHERE idProducto = ? ";

			PreparedStatement statement2 = jdbcConnection.prepareStatement(sql2);
			
			statement2.setInt(1, idpro);

			ResultSet resultSet2 = statement2.executeQuery();
			
			if(resultSet2.next()) {
				cuantos += resultSet2.getInt(1);
			}
			statement2.close();
			resultSet2.close();

		}
		cuantos*=10;	
		resultSet.close();
		statement.close();
		
		 sql = "SELECT COUNT (*) FROM Venta";

		 statement = jdbcConnection.prepareStatement(sql);
		
		 resultSet = statement.executeQuery();
		
		if(resultSet.next()) {
			cuantos2 = resultSet.getInt(1);
		}
		resultSet.close();
		statement.close();
		if(cuantos2!=0) {

			System.out.print("Calificacion del proveedor "+ idprov + " es " + (int)(cuantos/cuantos2) + "/10.\n");
			 sql = "UPDATE Proveedor set Calificacion = ? WHERE idProveedor = ?";

			 statement = jdbcConnection.prepareStatement(sql);
			 
			statement.setInt(1, (int)(cuantos/cuantos2));
			statement.setInt(2, idprov);

			statement.executeUpdate();

			statement.close();
		}else {
			System.out.print("No hay ventas.\n");
		}
	}
	
	
	public boolean estaEliminado(int idCliente) throws SQLException {
		int cuantos = -1;
		String sql = " SELECT COUNT (*) from Eliminado where idCliente=? ";
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, idCliente);

		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next()) {
			cuantos = resultSet.getInt(1);
		}
		
		resultSet.close();
		statement.close();
		
		if(cuantos == 0) {
			return false;
			
		}else {
			
			return true;
		}

	}
}

