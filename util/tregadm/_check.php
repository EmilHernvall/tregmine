<?php

if (!array_key_exists("online", $_SESSION)) {
    header('Location: index.php');
    exit;
}
