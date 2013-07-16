<?php

require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';
require_once '_password.php';

session_destroy();
header('Location: index.php');
