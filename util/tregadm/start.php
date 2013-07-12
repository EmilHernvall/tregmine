<?php
require_once '_init.php';
require_once '_check.php';
?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Tregmine Admin Tool</title>
    <style type="text/css">
    @import 'style.css';
    </style>

  <link rel="stylesheet" href="jquery-ui.css" />
  <script src="jquery-1.9.1.js"></script>
  <script src="jquery-ui.js"></script>
  <script src="names.php"></script>

</head>
<body>
    <div id="layout_wrapper">
        <h1>Tregmine Admin Tool</h1>

        <?php require 'menu.php'; ?>

        <p>
            Welcome!
        </p>

        <h2>User quicksearch</h2>

        <form method="get" action="search.php">
            <div class="fieldwrapper">
                <label for="q">User</label>
                <div class="field">
                    <input type="text" name="q" id="q" />
                </div>
            </div>

            <div class="buttonwrapper">
                <button type="submit">Search</button>
            </div>
        </form>
    </div>
</body>
</html>
