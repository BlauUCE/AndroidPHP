<?php
/////////////////////////////////////
// CONTROLADOR LOC
/////////////////////////////////////
include "config.php";
include "utils.php";

$dbConn =  connect($db);

//  listar todos los user o solo uno
if ($_SERVER['REQUEST_METHOD'] == 'GET')
{
    if (isset($_GET['id']))
    {
      //Enviar loc's de un user_id
      $sql = $dbConn->prepare("SELECT * FROM loc where user_id=:id");
      $sql->bindValue(':id', $_GET['id']);
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");
      echo json_encode( $sql->fetchAll() );
      exit();
	}
}


// Nuevo loc post/put/delete
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    //obtener body del POST
    $entityBody = file_get_contents('php://input');
    $body_post =  json_decode($entityBody, true);
    
    //var_dump($body_post);

    
    //verificar put/delete
    if (isset($_GET['id']))
    {
        if(isset($body_post['del']))
            del($body_post['del']);
  
        try {
            $sql = "INSERT INTO loc (user_id, lat, lon) VALUES (:user_id, :lat, :lon)";
            $statement = $dbConn->prepare($sql);
            $statement = bindAllValues($statement, $body_post);
            $statement->execute();
            header("HTTP/1.1 200 OK");
            echo '{"ok":1}';
            exit();

            //header("HTTP/1.1 200 OK");
            //exit();

            //header('Content-type: application/json');
            //echo json_encode( array(1) );
            exit();

        } catch( Exception $e) {
            echo var_dump($e);
        }
    }
}


function del($id_del) 
{
    //echo "DELETE";
    global $dbConn;
    $statement = $dbConn->prepare("DELETE FROM loc where user_id=:id");
    $statement->bindValue(':id', $id_del);
    $statement->execute();
    header("HTTP/1.1 200 OK");
    exit();
}

//En caso de que ninguna de las opciones anteriores se haya ejecutado
header("HTTP/1.1 400 Bad Rqst");

?>