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
    $body_post =  json_decode($entityBody, true);
    
    //var_dump($body_post);

    //verificar put/delete
    if (isset($_GET['id']))
    {
        if(isset($body_post['put']))
            put();
        if(isset($body_post['del']))
            del($_GET['id']);
        if(isset($body_post['log']))
            login();
    }
    else {
        $sql = "INSERT INTO user (name, pass, type) VALUES (:name, :pass, :type)";
        $statement = $dbConn->prepare($sql);
        $statement = bindAllValues($statement, $body_post);
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

//login
function login() {
  global $dbConn;
  $sql = $dbConn->prepare("SELECT * FROM user");
  $sql->execute();
  $sql->setFetchMode(PDO::FETCH_ASSOC);
  $users = json_encode( $sql->fetchAll() );

  //obtener body del POST
  $entityBody = file_get_contents('php://input');
  $body_post =  json_decode($entityBody, true);

  $usersA = json_decode($users);

  //var_dump($usersA);
  //var_dump($body_post);
  //print_r(array_search("prueba", $usersA, false)); 
  //var_dump(in_array("prueba", $usersA));
  //var_dump(array_column($usersA, 'name'));
  //echo array_search('prueba', $users);
  $ind = array_search($body_post['user'], array_column($usersA, 'name'));
  //var_dump($body_post['pass']);
  //echo $body_post['user'];
  //print_r(array_keys(array_column($usersA, 'name'), $body_post['user']));
  //var_dump( $ind );
  $keys = array_keys(array_column($usersA, 'name'), $body_post['user']);
  //echo "\n";
  //recorrer usuarios de igual nombre
  foreach($keys as $key) {
    //print_r($usersA[$key]); // escribir los usuarios de igual nombre
    if($usersA[$key]->pass == $body_post['pass']) {
      header("HTTP/1.1 296 OK");
      http_response_code(296);
      echo json_encode(array($usersA[$key]->id), JSON_FORCE_OBJECT);
      //echo $usersA[$key]->id;
      exit();  
    }
  }
  //no coincide clave con ningun usuario, acceso negado
  header("HTTP/1.1 295 OK");
  http_response_code(295);
  echo json_encode(array(-1), JSON_FORCE_OBJECT);
  //echo -1;
  exit();  
  //$usr = array_values($usersA);
  //var_dump( $usr );
 /*  if ($ind === FALSE) {
    echo 'no';
    http_response_code(295);
    exit();  
  }
  else { 
    echo $usr[$ind]->id;
    http_response_code(296);
    exit();  
  } */
  //header("HTTP/1.1 200 OK");
  //exit();
}

//Actualizar
function put()
{
  //echo 'REQUEST_METHOD PUT';
  global $dbConn;
  //obtener body del POST
  $entityBody = file_get_contents('php://input');
  $body_post =  json_decode($entityBody, true);
   
  $input = $_GET;
  $postId = $input['id'];
  
  //var_dump($body_post);

  unset($body_post['put']);

  //var_dump($body_post);

  $sql = "UPDATE user SET name=:name, pass=:pass, type=:type WHERE id='$postId'";

    try {
      $statement = $dbConn->prepare($sql);
    } catch(Exception $e) {
      echo "falla sql";
      var_dump($e);
    }

  try {
    bindAllValues($statement, $body_post);
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
header("HTTP/1.1 400 Bad R");

?>