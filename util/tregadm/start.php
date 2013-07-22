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
        <h1 id="banner"><span>Tregmine Admin Tool</span></h1>

        <?php require 'menu.php'; ?>

        <h2 class="info">Welcome to Tregmine!</h2>

        <div class="col75">
            <h3 class="infoHeader">User quicksearch</h3>

            <form method="get" action="search.php">
                <div class="field">
                    <label for="q">User</label>
                    <div class="element">
                        <input type="text" name="q" id="q" />
                    </div>
                    <div class="end">&nbsp;</div>
                </div>

                <div class="button">
                    <button type="submit">Search</button>
                </div>
            </form>

            <h3 class="infoHeader">Zone quicksearch</h3>

            <form method="get" action="zones.php">
                <div class="field">
                    <label for="q">Zone</label>
                    <div class="element">
                        <input type="text" name="q" id="q" />
                    </div>
                    <div class="end">&nbsp;</div>
                </div>

                <div class="button">
                    <button type="submit">Search</button>
                </div>
            </form>
        </div>

        <div class="col_clear">&nbsp;</div>
    </div>
</body>
</html>
