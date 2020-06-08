<?php
/////////////////////////////////////
// CONTROLADOR CHAT
/////////////////////////////////////
include "config.php";
include "utils.php";

$dbConn =  connect($db);

//  listar todos los chat o solo uno
if ($_SERVER['REQUEST_METHOD'] == 'GET')
{
    if (isset($_GET['id']))
    {
      //Enviar un chat
      $sql = $dbConn->prepare("SELECT * FROM chat where id=:id");
      $sql->bindValue(':id', $_GET['id']);
      $sql->execute();
      header("HTTP/1.1 200 OK");
      echo json_encode(  $sql->fetch(PDO::FETCH_ASSOC)  );
      exit();
    }
    else {
      //Enviar todos los chats
      $sql = $dbConn->prepare("SELECT * FROM chat");
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");
      echo json_encode( $sql->fetchAll() );
      exit();
	}
}


// Nuevo chat post/put/delete
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    //obtener body del POST
    $entityBody = file_get_contents('php://input');
    $vals =  json_decode($entityBody, true);

    /*switch(json_last_error()) {
      case JSON_ERROR_NONE:
          echo ' - Sin errores';
      break;
      case JSON_ERROR_DEPTH:
          echo ' - Excedido tamaño máximo de la pila';
      break;
      case JSON_ERROR_STATE_MISMATCH:
          echo ' - Desbordamiento de buffer o los modos no coinciden';
      break;
      case JSON_ERROR_CTRL_CHAR:
          echo ' - Encontrado carácter de control no esperado';
      break;
      case JSON_ERROR_SYNTAX:
          echo ' - Error de sintaxis, JSON mal formado';
      break;
      case JSON_ERROR_UTF8:
          echo ' - Caracteres UTF-8 malformados, posiblemente codificados de forma incorrecta';
      break;
      default:
          echo ' - Error desconocido';
      break;
    }*/
    
    //verificar put/delete
    if (isset($_GET['id']))
    {
        if(isset($vals['put']))
            put();
        if(isset($vals['del']))
            del($_GET['id']);
    }
    else {  
        $sql = "INSERT INTO chat (user_id, content, type) VALUES (:user_id, :content, :type)";
        $statement = $dbConn->prepare($sql);
        $statement = bindAllValues($statement, $vals);
        try {
          $statement->execute();     
        } catch(Exception $e) {
          echo "falla statement->execute()";
          var_dump($e);
        }
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
  
  unset($vals['put']);

  $sql = "
    UPDATE chat
    SET user_id=:user_id, content=:content, type=:type
    WHERE id='$postId'";

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
    $statement = $dbConn->prepare("DELETE FROM chat where id=:id");
    $statement->bindValue(':id', $id_del);
    $statement->execute();
	  header("HTTP/1.1 200 OK");
	  exit();
}

//En caso de que ninguna de las opciones anteriores se haya ejecutado
header("HTTP/1.1 400 Bad Request");

?>