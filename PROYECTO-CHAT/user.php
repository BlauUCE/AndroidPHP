<?php
/////////////////////////////////////
// CONTROLADOR USER
/////////////////////////////////////
include "config.php";
include "utils.php";

$dbConn =  connect($db);

//  listar todos los user o solo uno
if ($_SERVER['REQUEST_METHOD'] == 'GET')
{
    if (isset($_GET['id']))
    {
      //Enviar un user
      $sql = $dbConn->prepare("SELECT * FROM user where id=:id");
      $sql->bindValue(':id', $_GET['id']);
      $sql->execute();
      header("HTTP/1.1 200 OK");
      echo json_encode(  $sql->fetch(PDO::FETCH_ASSOC)  );
      exit();
    }
    else {
      //Enviar todos los chats
      $sql = $dbConn->prepare("SELECT * FROM user");
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");
      echo json_encode( $sql->fetchAll() );
      exit();
	}
}


// Nuevo user post/put/delete
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    //obtener body del POST
    $entityBody = file_get_contents('php://input');
    $vals =  json_decode($entityBody, true);
    
    //var_dump($vals);

    //verificar put/delete
    if (isset($_GET['id']))
    {
        if(isset($vals['put']))
            put();
        if(isset($vals['del']))
            del($_GET['id']);
    }
    else {
        $sql = "INSERT INTO user (name, pass, type) VALUES (:name, :pass, :type)";
        $statement = $dbConn->prepare($sql);
        $statement = bindAllValues($statement, $vals);
        $statement->execute();
        $postId = $dbConn->lastInsertId();
        if($postId)
        {
            $input['id'] = $postId;
            header("HTTP/1.1 200 OK");
            echo json_encode($input);
            exit();
        }
    }
}


//Actualizar
function put()
{
  //echo 'REQUEST_METHOD PUT';
  global $dbConn;
  //obtener body del POST
  $entityBody = file_get_contents('php://input');
  $vals =  json_decode($entityBody, true);
   
  $input = $_GET;
  $postId = $input['id'];
  
  //var_dump($vals);

  unset($vals['put']);

  //var_dump($vals);

  $sql = "UPDATE user SET name=:name, pass=:pass, type=:type WHERE id='$postId'";

    try {
      $statement = $dbConn->prepare($sql);
    } catch(Exception $e) {
      echo "falla sql";
      var_dump($e);
    }

  try {
    bindAllValues($statement, $vals);
  } catch(Exception $e) {
    echo "falla bindAllValues";
    var_dump($e);
  }
  
  try {
    $statement->execute();     
  } catch(Exception $e) {
    echo "falla statement->execute()";
    var_dump($e);
  }
  header("HTTP/1.1 200 OK");
  exit();
}

function del($id_del) 
{
    //echo "DELETE";
    global $dbConn;
    $statement = $dbConn->prepare("DELETE FROM user where id=:id");
    $statement->bindValue(':id', $id_del);
    $statement->execute();
    header("HTTP/1.1 200 OK");
    exit();
}

//En caso de que ninguna de las opciones anteriores se haya ejecutado
header("HTTP/1.1 400 Bad Request");

?>