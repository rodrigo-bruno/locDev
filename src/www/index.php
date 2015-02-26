<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>locDev</title>
    <?php
	date_default_timezone_set('Europe/Lisbon');
    //Function to get macaddress
    function getMACAddress()
    {
        $mac = exec('cat /proc/net/arp | awk \'/'.$_SERVER['REMOTE_ADDR'].'/{print $4}\'');
	return str_replace(":", "", $mac);
    }
    //Get course info
    $handle = @fopen("course/meta-info", "r");
    if($handle)
    {
        $course_name = fgets($handle);
        while(($buffer = fgets($handle)) != false)
        {
            $shift = explode(";", $buffer);
            $shifts[$shift[0]] = $shift;
        }
        unset($shift);
        unset($buffer);
    }
    //If confirm button pressed
    if(isset($_POST['confirm']))
    {
        if(!empty($_POST['login']) and !empty($_POST['password']) 
            and !empty($_POST['shift']) and !empty($_POST['pin_password']))
        {
            if(file_exists('students/'.$_POST['login']))
            {
                $buffer = file_get_contents('students/'.$_POST['login']);
                if (strcmp($buffer,hash('md5',$_POST['password'])) == 0)
                {
                    $today = getdate();
                    $shift_start_minutes = $shifts[$_POST['shift']][4]*60 + $shifts[$_POST['shift']][5];
                    $shift_startPdelay_minutes = $shift_start_minutes + $shifts[$_POST['shift']][7]*60 + $shifts[$_POST['shift']][8];
                    $current_minutes = $today[hours]*60 + $today[minutes];
                    if($shift_start_minutes < $current_minutes and $shift_startPdelay_minutes > $current_minutes)
                    {
                        $content = $shifts[$_POST['shift']][0].$shifts[$_POST['shift']][1].$today['mday'].$today['mon'].$today['year'];
                        $pin = substr(hash('md5',$content), 0, 4);
			
            // DEBUG ONLY!
                        echo $pin;
                        if (strcmp($pin,strtolower($_POST['pin_password'])) == 0)
                        {
                            $format = 'course/shifts/%s/%d-%d-%d-%d-%d/';
                            $folder = sprintf($format,$_POST['shift'],$today['year'],$today['mon'],$today['mday'],$shifts[$_POST['shift']][4],$shifts[$_POST['shift']][5]);
                            $attendancesFile = $folder.'attendances.dat';
                            $macFile = $folder.'macs/'.getMACAddress();
				
                // Windows version
			    //if(!file_exists($macFile))
			    //{
                            if (file_exists($attendancesFile))
                            {
                                $content = ';'.$_POST['login'];
                            }
                            else
                            {
                                $content = $_POST['login'];
                            }
                            $ret = file_put_contents($attendancesFile,$content, FILE_APPEND | LOCK_EX);
                            if ($ret > 0)
                            {
                                echo "<script>alert(\"Attendance confirmed!\")</script>";
                            }
                            else 
                            {
                                echo "<script>alert(\"Error in file_put_contents\")</script>";
                            }
                            unset($today,$content,$ret,$attendancesFile);
					
                    // Windows version			
			        // touch($macFile);
                            unset($today,$content,$pin);
                            echo "<script>alert(\"Attendance confirmed!\")</script>";
		        //    }
				// Windows version
			    //else
			    //{
			    //    echo "<script>alert(\"MAC already used!\")</script>";
			    //}
                        }
                        else
                        {
                            echo "<script>alert(\"The pin is not correct!\")</script>";
                        }
                    }
                    else
                    {
                        echo "<script>alert(\"Attendance not valid (class time off limits)!\")</script>";
                    }
                }
                else
                {
                    echo "<script>alert(\"The password is incorrect!\");</script>";
                }
            }
            else
            {
                echo "<script>alert(\"Student with id ".$_POST['login']." doesnt exist\");</script>";
            }
        }
        else
        {
            echo "<script>alert(\"All fields are required\")</script>";
        }
    }
    ?>         
</head>
<body bgcolor="#0ca3d2">
    <table width="723" align="center" border="0" bgcolor="#CACAFF">
        <td>
            <h1 align="center"><font color="#000066">locDev - Automatic Presence Registration System</font></h1>
        </td>
    </table>
    <table width="723" align="center" border="0" bgcolor= "#DFDFFF">
        <td align="center"><a href="create.php"><font color="#0000FF">Create Account</font></a></td>
        <td align="center"><a href="index.php"><font color="#0055FF">Confirm Attendance</font></a></td>
        <td align="center"><a href="professor.php"><font color="#0055FF">Professor Area</font></a></td>
    </table>
    <table width="723" align="center" bgcolor="#FFFFFF">
        <td>
            <h3><font color="#000099">Confirm Attendance</font></h3>
            <form method="post" action="index.php">
            <p>
                <font color="#000099">IST ID : <input type="text" name="login" value="" style="width:6%"></font> 
                <font color="#000099"> &nbsp;&nbsp;&nbsp;Password : <input type="password" name="password" value=""></font> 
            </p>
            <h6>
                <font color="#000099">IST ID MUST BE 55065, NOT ist55065</font>
            </h6>
            <p>
                <font color="#000099">Shift : &nbsp;&nbsp;   
                <select name="shift">
                    <?php
                        if(!empty($shifts))
                        {
                            foreach ($shifts as &$shift)
                            {
                                echo "<option value=$shift[0]>$shift[0], Type:$shift[2] Weekday:$shift[3] Time:$shift[4]h$shift[5] Room:$shift[6]</option>";
                            }
                        }
                    ?>
                </select>
                </font>
            </p>
            <p>
                <font color="#000099">Pin : <input type="password" name="pin_password" value="" style="width:4%"></font>
            </p>
            <h6>
                <font color="#000099">All Fields are required.</font>
            </h6>
            <p>
                <input type="submit" name="confirm" value="Confirme Attendance">
            </p>
            </form>
        </td>
        <td>&nbsp;</td>
    </table>
</body>
</html>
