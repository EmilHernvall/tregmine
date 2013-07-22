<?php
require_once '_init.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {

    $username = $_POST["username"];
    $password = $_POST["pass"];

    $stmt = $conn->prepare("SELECT * FROM player WHERE player_name = ?");
    $stmt->execute(array($username));

    $user = $stmt->fetch();

    $stmt->closeCursor();

    if (!$user) {
        header('Location: index.php?error=fail');
        exit;
    }

    if (crypt($password, $user["player_password"]) != $user["player_password"]) {
        header('Location: index.php?error=fail');
        exit;
    }

    $stmt = $conn->prepare("SELECT * FROM player_property WHERE player_id = ?");
    $stmt->execute(array($user["player_id"]));

    $properties = $stmt->fetchAll(PDO::FETCH_ASSOC);

    $_SESSION["online"] = true;
    $_SESSION["id"] = $user["player_id"];

    foreach ($properties as $property) {
        $_SESSION[$property["property_key"]] = $property["property_value"];
    }

    if (!array_key_exists("admin", $_SESSION) &&
        !array_key_exists("mentor", $_SESSION)) {

        session_destroy();
    }

    header('Location: start.php');
    exit;
}
?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Tregmine Admin Tool</title>
    <style type="text/css">
    @import 'style.css';
    </style>
</head>
<body>
    <div id="layout_wrapper">
        <h1 id="banner"><span>Tregmine Admin Tool</span></h1>

        <?php if (array_key_exists("error", $_GET)): ?>
            <p>
                <?php if ($_GET["error"] == "fail"): ?>
                    Invalid username or password.
                <?php endif;?>
            </p>
        <?php endif; ?>

        <form method="post" action="index.php">
            <h3 class="formHeader">Login</h3>

            <div class="field">
                <label for="username">Username</label>
                <div class="element">
                    <input type="text" name="username" id="username" />
                </div>
                <div class="end">&nbsp;</div>
            </div>

            <div class="field">
                <label for="password">Password</label>
                <div class="element">
                    <input type="password" name="pass" id="pass" />
                </div>
                <div class="end">&nbsp;</div>
            </div>

            <div class="button">
                <button type="submit">Login</button>
            </div>
        </form>
    </div>
</body>
</html>
