CREATE TABLE Proveedor(
				idProveedor int Primary Key not null,
				Calificacion INT NULL );


CREATE TABLE Producto(
				idProducto  INT PRIMARY KEY  NOT NULL,
				Fecha Date,
				Stock INT,
				idProveedor INT NOT NULL, 
				Precio FLOAT, 
				FOREIGN KEY(idProveedor) REFERENCES Proveedor (idProveedor)
				);

CREATE TABLE Cliente(
				idCliente  INT PRIMARY KEY not null ,
				Nombre VARCHAR2(25),
				Correo VARCHAR2(25) UNIQUE,
				Telefono INT
				);


CREATE TABLE Venta(
				idVenta  INT PRIMARY KEY not null,
				idCliente  INT NOT NULL,
				idProducto INT NOT NULL,
				Cantidad INT NOT NULL, 
				FOREIGN KEY(idCliente) REFERENCES Cliente (idCliente),
				FOREIGN KEY(idProducto) REFERENCES Producto (idProducto)
				);


CREATE TABLE Reserva(
				idReserva  INT PRIMARY KEY not null,
				idCliente  INT NOT NULL,
				idProducto INT NOT NULL,
				Cantidad INT NOT NULL,
				FOREIGN KEY(idCliente) REFERENCES Cliente (idCliente),
				FOREIGN KEY(idProducto) REFERENCES Producto (idProducto)
				);


CREATE TABLE Completada( 
				idReserva  INT PRIMARY KEY not null,
				FOREIGN KEY(idReserva) REFERENCES Reserva (idReserva)
				);

CREATE TABLE Cancelada( 
				idReserva  INT PRIMARY KEY not null,
				FOREIGN KEY(idReserva) REFERENCES Reserva (idReserva)
				);



CREATE TABLE Liquidacion(
				idLiquidacion  INT PRIMARY KEY NOT NULL,
				NumMeses  INT NOT NULL,
				Descuento INT NOT NULL
				);

CREATE TABLE Amortiza(
				idLiquidacion  INT NOT NULL,
				idProducto INT NOT NULL,
				Precio FLOAT NOT NULL,
				PRIMARY KEY (idLiquidacion, IdProducto),
				FOREIGN KEY(idLiquidacion) REFERENCES Liquidacion (idLiquidacion),
				FOREIGN KEY(idProducto) REFERENCES Producto (idProducto)
				);

CREATE TABLE Promocion(
				Cantidad  INT NOT NULL,
				Precio FLOAT NOT NULL,
				TiempoExp INT NOT NULL,
				idProducto INT NOT NULL,
				PRIMARY KEY (Cantidad, Precio, TiempoExp),
				FOREIGN KEY(idProducto) REFERENCES Producto (idProducto)
				);


CREATE TABLE Pedido(
				idPedido INT PRIMARY KEY  NOT NULL,
				idProducto INT NOT NULL,
				Cantidad INT NOT NULL, 
				FOREIGN KEY(idProducto) REFERENCES Producto (idProducto)
				);


CREATE TABLE Recibido( 
				idPedido INT PRIMARY KEY  NOT NULL,
				FOREIGN KEY(idPedido) REFERENCES Pedido (idPedido)
				);

CREATE TABLE Devuelto( 
				idPedido INT PRIMARY KEY  NOT NULL,
				FOREIGN KEY(idPedido) REFERENCES Pedido (idPedido)
				);

CREATE TABLE Eliminado( 
				idCliente INT PRIMARY KEY  NOT NULL,
				FOREIGN KEY(idCliente) REFERENCES Cliente (idCliente)
				);				

INSERT INTO Proveedor (idProveedor) VALUES (1);
INSERT INTO Proveedor (idProveedor) VALUES (2);
INSERT INTO Proveedor (idProveedor) VALUES (3);
INSERT INTO Proveedor (idProveedor) VALUES (4);
INSERT INTO Proveedor (idProveedor) VALUES (5);
INSERT INTO Proveedor (idProveedor) VALUES (6);
INSERT INTO Proveedor (idProveedor) VALUES (7);
INSERT INTO Proveedor (idProveedor) VALUES (8);
INSERT INTO Proveedor (idProveedor) VALUES (9);
INSERT INTO Proveedor (idProveedor) VALUES (10);

INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (1,TO_DATE('2021/01/01', 'yyyy/mm/dd'),3,1,10.0);
INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (2,TO_DATE('2021/01/01', 'yyyy/mm/dd'),5,2,25.0);
INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (3,TO_DATE('2021/01/01', 'yyyy/mm/dd'),7,6,30.0);
INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (4,TO_DATE('2021/01/01', 'yyyy/mm/dd'),9,4,40.0);
INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (5,TO_DATE('2021/01/01', 'yyyy/mm/dd'),11,8,20.0);
INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (6,TO_DATE('2021/01/01', 'yyyy/mm/dd'),13,7,55.0);
INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (7,TO_DATE('2021/01/01', 'yyyy/mm/dd'),15,9,25.0);
INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (8,TO_DATE('2021/01/01', 'yyyy/mm/dd'),17,1,78.0);
INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (9,TO_DATE('2021/01/01', 'yyyy/mm/dd'),21,5,30.0);
INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (10,TO_DATE('2021/01/01', 'yyyy/mm/dd'),25,2,70.0);

INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (11,TO_DATE('2008/05/08', 'yyyy/mm/dd'),25,2,150.0);
INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (12,TO_DATE('2005/06/05', 'yyyy/mm/dd'),25,6,26.0);
INSERT INTO Producto (idProducto,Fecha,Stock,idProveedor, Precio) VALUES (13,TO_DATE('2019/05/12', 'yyyy/mm/dd'),25,8,90.0);


create or replace TRIGGER COMPROBARLIQUIDACION 
BEFORE INSERT ON Amortiza
FOR EACH ROW
DECLARE
  cuantos int;
BEGIN
  SELECT COUNT(*) INTO cuantos FROM Amortiza WHERE idProducto= :new.idProducto;
  IF(cuantos!=0) THEN
       RAISE_APPLICATION_ERROR(-20011,'El producto no se puede liquidar de nuevo');
  END IF;
END;
/
create or replace TRIGGER COMPROBARPEDIDO 
BEFORE INSERT ON Devuelto
FOR EACH ROW
DECLARE
  cuantos int;
BEGIN
  SELECT COUNT(*) INTO cuantos FROM Recibido WHERE idPedido= :new.idPedido;
  IF(cuantos!=0) THEN
       RAISE_APPLICATION_ERROR(-20010,'El pedido no se puede devolver porque ya ha sido recibido');
  END IF;
END;
/
create or replace TRIGGER COMPROBARSTOCKRESERVA 
AFTER INSERT ON Completada
FOR EACH ROW
DECLARE
  cantidadpro INT;
  cantidadstock INT;
  idpro INT;
BEGIN
SELECT Cantidad INTO cantidadpro FROM Reserva WHERE idReserva = :new.idReserva;
SELECT idProducto INTO idpro FROM Reserva WHERE idReserva = :new.idReserva;
  SELECT Stock INTO cantidadstock FROM Producto WHERE idProducto= idpro;
  IF(cantidadstock>=cantidadpro) THEN
    UPDATE Producto set Stock = (cantidadstock - cantidadpro) WHERE idProducto= idpro;
   ELSE
       RAISE_APPLICATION_ERROR(-20005,'Stock insuficiente');
  END IF;
END;
/
create or replace TRIGGER COMPROBARSTOCKVENTA 
AFTER INSERT ON Venta
FOR EACH ROW
DECLARE
  cantidadpro INT;
BEGIN
  SELECT Stock INTO cantidadpro FROM Producto WHERE idProducto= :new.idProducto;
  IF(cantidadpro>=:new.Cantidad) THEN
    UPDATE Producto set Stock = (cantidadpro -:new.Cantidad) WHERE idProducto= :new.idProducto;
   ELSE
       RAISE_APPLICATION_ERROR(-20014,'Stock insuficiente');
  END IF;
END;
/
create or replace TRIGGER ELIMINARVENTA 
BEFORE INSERT ON Eliminado
FOR EACH ROW
DECLARE
  cuantos int;
BEGIN
  SELECT COUNT(*) INTO cuantos FROM Venta WHERE idCliente= :new.idCliente;
  IF(cuantos!= 0) THEN
       RAISE_APPLICATION_ERROR(-20015,'No se puede eliminar el cliente ya que ha hecho una compra');
  END IF;
END;