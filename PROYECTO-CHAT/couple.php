<?php
/////////////////////////////////////
// CONTROLADOR COUPLE
/////////////////////////////////////
include "config.php";
include "utils.php";

$dbConn =  connect($db);

//  listar todos los couple o solo uno
if ($_SERVER['REQUEST_METHOD'] == 'GET')
{
    //Enviar un couple
    if (isset($_GET['user_id1']) || isset($_GET['user_id2']))
    {
      if (isset($_GET['user_id1']) && !isset($_GET['user_id2'])) {
        $sql = $dbConn->prepare("SELECT * FROM couple where user_id1=:user_id1 or user_id2=:user_id1");
        $sql->bindValue(':user_id1', $_GET['user_id1']);
      } 
      if (isset($_GET['user_id2']) && !isset($_GET['user_id1'])) {
        $sql = $dbConn->prepare("SELECT * FROM couple where user_id1=:user_id2  or user_id2=:user_id2");
        $sql->bindValue(':user_id2', $_GET['user_id2']);
      }

      if (isset($_GET['user_id1']) && isset($_GET['user_id2'])) {
        $sql = $dbConn->prepare("SELECT * FROM couple where user_id1=:user_id1 and user_id2=:user_id2");
        $sql->bindValue(':user_id1', $_GET['user_id1']);
        $sql->bindValue(':user_id2', $_GET['user_id2']);
      }
      try {
        $sql->execute();
        $sql->setFetchMode(PDO::FETCH_ASSOC);
      } catch(Exception $e) {
        echo "falla get";
      }
      header("HTTP/1.1 200 OK");
      echo json_encode(  $sql->fetchAll()  );
      exit();
    }
    else {
      //Enviar todos los couple
      $sql = $dbConn->prepare("SELECT * FROM couple");
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");
      echo json_encode( $sql->fetchAll() );
      exit();
	}
}


// Nuevo couple post/put/delete
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    //obtener body del POST
    $entityBody = file_get_contents('php://input');
    $vals =  json_decode($entityBody, true);
    
    //verificar put/delete
    if (isset($_GET['user_id1']) && isset($_GET['user_id2']))
    {
        if(isset($vals['put']))
            put();
        if(isset($vals['del']))
            del($_GET['user_id1'], $_GET['user_id2']);
    }
    else {
        $sql = "INSERT INTO couple (user_id1, user_id2, name1, name2) VALUES (:user_id1, :user_id2, :name1, :name2)";
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
  $user1 = $input['user_id1'];
  $user2 = $input['user_id2'];
  
  unset($vals['put']);

  $sql = "
    UPDATE couple
    SET user_id1=:user_id1, user_id2=:user_id2
    WHERE user_id1=$user1 and user_id2=$user2";

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

function del($id_del1, $id_del2) 
{
    //echo "DELETE";
    global $dbConn;
    $statement = $dbConn->prepare("DELETE FROM couple where user_id1=:id1 and user_id2=:id2");
    $statement->bindValue(':id1', $id_del1);
    $statement->bindValue(':id2', $id_del2);
    $statement->execute();
    header("HTTP/1.1 200 OK");
    exit();
}

//En caso de que ninguna de las opciones anteriores se haya ejecutado
header("HTTP/1.1 400 Bad Request");

?>