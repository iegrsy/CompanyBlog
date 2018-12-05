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
        $row = mysqli_fetch_assoc($result);
        echo ('{"userid":' . $row["id"] . ', "islogin":true, "err":""}');
    } else {
        echo ('{"userid":-1, islogin":false, "err":"User not found."}');
    }

    mysqli_close($db);
}

function ListAllPost()
{
    $db = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
    // Check connection
    if (!$db) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $sql = "SELECT p.id, u.username, p.title, p.body, p.date FROM Posts p JOIN Users u ON p.userid = u.id ORDER BY p.date";
    $result = mysqli_query($db, $sql);

    if (mysqli_num_rows($result) > 0) {
        $list = '';
        // output data of each row
        while ($row = mysqli_fetch_assoc($result)) {
            if ($list != '') {
                $list = $list . ',';
            }

            $list = $list . '{"postid":' . $row["id"] . ', username":"' . $row["username"] . '", "title":"' .
                $row["title"] . '", "body":"' . $row["body"] . '", "date":"' . $row["date"] . '"}';
        }
        echo ("{\"posts\":[$list], \"err\":\"\"}");
    } else {
        echo ('{"posts":[], "err":"0 results"}');
    }

    mysqli_close($db);
}

function ListPostForUser($userid)
{
    $db = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
    // Check connection
    if (!$db) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $sql = "SELECT p.id, u.username, p.title, p.body, p.date FROM Posts p JOIN Users u ON u.id=\"$userid\" WHERE p.userid=u.id ORDER BY p.date";
    $result = mysqli_query($db, $sql);

    if (mysqli_num_rows($result) > 0) {
        $list = '';
        // output data of each row
        while ($row = mysqli_fetch_assoc($result)) {
            if ($list != '') {
                $list = $list . ',';
            }

            $list = $list . '{"postid":' . $row["id"] . ', username":"' . $row["username"] . '", "title":"' .
                $row["title"] . '", "body":"' . $row["body"] . '", "date":"' . $row["date"] . '"}';
        }
        echo ("{\"posts\":[$list], \"err\":\"\"}");
    } else {
        echo ('{"posts":[], "err":"0 results"}');
    }

    mysqli_close($db);
}

function ListCommentForPost($postid)
{
    $db = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
    // Check connection
    if (!$db) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $sql = "SELECT c.id, u.username, c.comment, c.date FROM Comments c JOIN Posts p ON c.postid=$postid JOIN Users u ON c.userid=u.id ORDER BY c.date";
    $result = mysqli_query($db, $sql);

    if (mysqli_num_rows($result) > 0) {
        $list = '';
        // output data of each row
        while ($row = mysqli_fetch_assoc($result)) {
            if ($list != '') {
                $list = $list . ',';
            }

            $list = $list . '{"commentid":' . $row["id"] . ', username":"' . $row["username"] . '", "comment":"' .
                $row["comment"] . '", "date":"' . $row["date"] . '"}';
        }
        echo ("{\"comments\":[$list], \"err\":\"\"}");
    } else {
        echo ('{"comments":[], "err":"0 results"}');
    }

    mysqli_close($db);
}

function AddUser($username, $email, $password, $birthday)
{
    $db = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
    // Check connection
    if (!$db) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $sql = "INSERT INTO Users (username, email, password, birthday) VALUES ('$username', '$email', '$password', '$birthday');";
    $result = mysqli_query($db, $sql);

    if ($result) {
        echo ('{"isadd":true, "err":""}');
    } else {
        echo ('{"isadd":false, "err":"' . mysqli_error($db) . '"}');
    }

    mysqli_close($db);
}

function AddPost($userid, $title, $body, $date)
{
    $db = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
    // Check connection
    if (!$db) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $sql = "INSERT INTO Posts (userid, title, body, date) VALUES ('$userid', '$title', '$body', '$date');";
    $result = mysqli_query($db, $sql);

    if ($result) {
        echo ('{"isadd":true, "err":""}');
    } else {
        echo ('{"isadd":false, "err":"' . mysqli_error($db) . '"}');
    }

    mysqli_close($db);
}

function AddComment($userid, $postid, $comment, $date)
{
    $db = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
    // Check connection
    if (!$db) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $sql = "INSERT INTO Comments (userid, postid, comment, date) VALUES ('$userid', '$postid', '$comment', '$date');";
    $result = mysqli_query($db, $sql);

    if ($result) {
        echo ('{"isadd":true, "err":""}');
    } else {
        echo ('{"isadd":false, "err":"' . mysqli_error($db) . '"}');
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
        ListAllPost();
    } else if ($action === "list_user_post") {
        $userid = $_POST["userid"];

        ListPostForUser($userid);
    } else if ($action === "list_all_post_of_comment") {
        $postid = $_POST["postid"];

        ListCommentForPost($postid);
    } else if ($action === "add_user") {
        $username = $_POST["username"];
        $email = $_POST["email"];
        $password = $_POST["password"];
        $birthday = $_POST["birthday"];

        AddUser($username, $email, $password, $birthday);
    } else if ($action === "add_post") {
        $userid = $_POST["userid"];
        $title = $_POST["title"];
        $body = $_POST["body"];
        $date = $_POST["date"];

        AddPost($userid, $title, $body, $date);
    } else if ($action === "add_comment") {
        $userid = $_POST["userid"];
        $postid = $_POST["postid"];
        $comment = $_POST["comment"];
        $date = $_POST["date"];

        AddComment($userid, $postid, $comment, $date);
    }
    exit();
}
?>