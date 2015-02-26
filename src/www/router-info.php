<html>
<body>

<p>
  <?php 
    echo "Test page for php on OpenWRT!";
  ?>
</p>

<?php 
  echo "<p>";
  echo "Server Name (IP): ";
  echo $_SERVER['SERVER_NAME'];
  echo "</p>";
  
  echo "<p>";
  echo "Client IP: ";
  echo $_SERVER['REMOTE_ADDR'];
  echo "</p>";
  
  echo "<p>";
  echo "User Agent: ";
  echo $_SERVER['HTTP_USER_AGENT'];
  echo "</p>";

  echo "<p>";
  echo "Remote Connection Port: ";
  echo $_SERVER['REMOTE_PORT'];
  echo "</p>";
?>

</body>
</html>
