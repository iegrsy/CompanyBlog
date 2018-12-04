<?php
define('DB_SERVER', 'localhost:3306');
define('DB_USERNAME', 'root');
define('DB_PASSWORD', '12345');
define('DB_DATABASE', 'mydb');

function Login($username, $email, $password)
{
    $db = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
    // Check connection
    if (!$db) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $sql = "SELECT id FROM Users WHERE ( username=\"$username\" OR email=\"$email\") AND password=\"$password\"";
    $result = mysqli_query($db, $sql);
    $count = mysqli_num_rows($result);

    if ($count == 1) {
        echo ('{"islogin":true, "err":""}');
    } else {
        echo ('{"islogin":false, "err":"User not found."}');
    }

    mysqli_close($db);
}

$action = $_GET["action"];
$data = $_POST["data"];

if (!$action) {
    exit();
} else {
    if ($action === "login") {
        $username = $_POST["username"];
        $email = $_POST["email"];
        $password = $_POST["password"];

        Login($username, $email, $password);
    } else if ($action === "list_all_post") {
        echo ("list_all_post");
    } else if ($action === "list_user_post") {
        echo ("list_user_post");
    } else if ($action === "list_all_post_of_comment") {
        echo ("list_all_post_of_comment");
    }
    exit();
}
?>