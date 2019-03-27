import time
from appium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
import unittest
import csv
import sys
import ObjectCollection as ObjColl


def main():
    #Initialize driver session with the device and get driver handle to use later in the code.
    driver = InitializeSession()

    #Create CSV file for output results
    ResultFile=sys.argv[11]
    csv_file = open(ResultFile, mode='w')
    fieldnames = ['Test case #', 'Test Result', 'Remarks']
    writer = csv.DictWriter(csv_file, fieldnames=fieldnames)
    writer.writeheader()


    #Test for selecting a loo, see directions and update rating.
    Test1(driver,writer)

    #Test for adding new loo information.
    #Test2(driver,writer)

    #Test for adding cleaning details
    Test3(driver,writer)

    #Test for modifying existing Janitor info
    Test4(driver,writer)

    #Close CSV file
    csv_file.close()

    #Close test session with the server.
    CloseSession(driver)

def InitializeSession():
        SysPort=sys.argv[1]
        Udidname=sys.argv[2]
        PlatformVer=sys.argv[3]
        AvdName=sys.argv[4]
        DeviceName=sys.argv[5]
        PlatformName=sys.argv[6]
        AutomationName=sys.argv[7]
        Appname=sys.argv[8]
        AppWaitDuration=sys.argv[9]
        DeviceReadyTimeout=sys.argv[10]

        DesiredCaps = {
        'platformName': PlatformName,
        'platformVersion': PlatformVer,
        'deviceName': DeviceName,
        'automationName': AutomationName,
        'app': Appname,
        'appWaitDuration': AppWaitDuration,
        'appPackage': 'com.example.cloo2019',
        'appActivity': '.FullscreenActivity',
        'deviceReadyTimeout': DeviceReadyTimeout,
        'avd': AvdName,
        'systemPort':SysPort,
        'udid':Udidname,
        'unicodeKeyboard':False,
        'resetKeyboard':False,
        'autoGrantPermissions': True
        }

        driver = webdriver.Remote('http://192.168.0.6:4723/wd/hub', DesiredCaps)
        return driver


def Test1(driver,writer):

    #Select Allow button to access location services if prompted.
    # if (ObjColl.ClickButton(driver,'com.android.packageinstaller:id/permission_allow_button') == -1):
    #     TestRemarks = "Test 1 failed: Could not locate allow button in the flow."
    #     ObjColl.WriteResult(writer,"Test 1","FAILED",TestRemarks)
    #     #print(TestResult,flush=True)
    #     return -1

    time.sleep(5)
    # Invoke a loo location from the list view to see directions and update rating and/or comments.
    # Invokes second record from the list. In this screen, there are many textviews. Instance 4 corresponds to second item.
    #element = driver.find_element_by_android_uiautomator('new UiSelector().className("android.widget.TextView").instance(4)')
    ObjColl.SelectLooFromList(driver,4)

    #Select Direction button to see the map
    #ObjColl.ClickDirection(driver)
    ObjColl.ClickButton(driver,'com.example.cloo2019:id/button_Navigate')
    #Select Back button to choose rating, submit and close.
    time.sleep(10)
    driver.press_keycode(4)
    time.sleep(3)

    # Update rating and comments
    # Co-ordinates for Ratingbar
    # 5: 580,120
    # 4: 460,120
    # 3: 360,120
    # 2: 240,120
    # 1: 100,120
    if(ObjColl.SelectRating(driver,360,120) == -1):
        TestRemarks = "Test 1 failed: Could not locate the Rating bar button from the navigation window."
        ObjColl.WriteResult(writer,"Test 1","FAILED",TestRemarks)
        #print(TestResult,flush=True)
        return -1


    #ObjColl.ClickSubmit(driver)
    ObjColl.ClickButton(driver,'com.example.cloo2019:id/button_submit_close')
    # Select Back button to go back to Loo list view screen.
    driver.press_keycode(4)
    time.sleep(2)


    TestRemarks = "Test 1 passed:"
    ObjColl.WriteResult(writer,"Test 1", "PASSED", TestRemarks)
    #print(TestResult, flush=True)
    return 0


def Test2(driver,writer):

    #Select Allow button to access location services if prompted.
    #ObjColl.ClickAllowButton(driver)
    #time.sleep(40)
    #Add new loo rating and comments
    ObjColl.ClickButton(driver,'com.example.cloo2019:id/fab')

    driver.press_keycode(4)

    #Add new toilet name and address
    ObjColl.AddTextById(driver,'com.example.cloo2019:id/editTextToiletName','New Toilet from automation - 1')
    ObjColl.AddTextById(driver,'com.example.cloo2019:id/editTextAddress','Automation address - 1')

    #driver.press_keycode(4)
    #Select toilet access from the drop down.
    ObjColl.SelectFromDropDown(driver,"com.example.cloo2019:id/spinner_toiletAccessUser",4)

    #Select gender type from the drop down.
    ObjColl.SelectFromDropDown(driver,"com.example.cloo2019:id/spinner_GenderUser",2)

    #Select toilet type
    ObjColl.SelectCheckBox(driver, "com.example.cloo2019:id/checkBoxCommodeUser")

    #Scroll to next page on the screen
    ObjColl.ScrollSimulation(driver,"android.view.ViewGroup",400,1000,400,700)

    #Co-ordinates for Ratingbar
    #5: 580,120
    #4: 460,120
    #3: 360,120
    #2: 240,120
    #1: 100,120
    ObjColl.SelectRating(driver,100,120)

    #Add some comments
    ObjColl.AddTextById(driver,'com.example.cloo2019:id/editTextComments','Add comments to this rating.')

    #Save the updates
    ObjColl.ClickButton(driver,'com.example.cloo2019:id/buttonUserSubmit')

    TestRemarks = "Test 2 passed:"
    ObjColl.WriteResult(writer,"Test 2", "PASSED", TestRemarks)
    #print(TestResult, flush=True)
    return 0


def Test3(driver,writer):
    #Add cleaning details for 'Basics' section and remarks of an existing loo
    #element = driver.find_element_by_android_uiautomator(
    #    'new UiSelector().className("android.widget.TextView").instance(4)')

    #Select Allow button to access location services if prompted.
    #ObjColl.ClickAllowButton(driver)


    # time.sleep(2)
    # driver.swipe(50,800,50,400,1500)
    # time.sleep(2)
    #time.sleep(40)
    #Select a toilet from the list
    ObjColl.SelectLooFromList(driver,4)

    ObjColl.ClickButton(driver,"com.example.cloo2019:id/buttonClean")
    time.sleep(2)
    driver.press_keycode(4)
    time.sleep(1)
    ObjColl.SelectCheckBox(driver,"com.example.cloo2019:id/checkBoxWater")
    ObjColl.SelectCheckBox(driver,"com.example.cloo2019:id/checkBoxElectricity")
    ObjColl.SelectCheckBox(driver,"com.example.cloo2019:id/checkBoxStinks")
    ObjColl.SelectCheckBox(driver,"com.example.cloo2019:id/checkBoxPlumbing")

    ObjColl.AddTextById(driver,'com.example.cloo2019:id/editTextOthers','All basics check boxes ticked')

    ObjColl.ClickButton(driver,"com.example.cloo2019:id/button_Cleaned")

    TestRemarks = "Test 3 passed:"
    ObjColl.WriteResult(writer,"Test 3", "PASSED", TestRemarks)
    #print(TestResult, flush=True)
    return 0



def Test4 (driver,writer):

    #Select Allow button to access location services if prompted.
    #ObjColl.ClickAllowButton(driver)

    #Select a toilet from the list
    ObjColl.SelectLooFromList(driver,4)

    #Update existing Janitor details.
    ObjColl.ClickButton(driver,"com.example.cloo2019:id/button_Janitor")

    time.sleep(2)
    driver.press_keycode(4)
    time.sleep(1)

    #Select Toilet access from the drop down.
    ObjColl.SelectFromDropDown(driver,"com.example.cloo2019:id/spinner_toiletAccess",2)

    #Select Gender from the drop down
    ObjColl.SelectFromDropDown(driver,"com.example.cloo2019:id/spinner_Gender",2)

    #Select toilet type
    ObjColl.SelectCheckBox(driver, "com.example.cloo2019:id/checkBoxCommode")

    #Select Features
    ObjColl.SelectCheckBox(driver, "com.example.cloo2019:id/checkBox_Water")
    ObjColl.SelectCheckBox(driver, "com.example.cloo2019:id/checkBox_Soap")

    #Save the details
    ObjColl.ClickButton(driver,"com.example.cloo2019:id/buttonSave")

    #Move back to list view screen
    driver.press_keycode(4)
    driver.press_keycode(4)

    TestRemarks = "Test 4 passed:"
    ObjColl.WriteResult(writer,"Test 4", "PASSED", TestRemarks)
    #print(TestResult, flush=True)
    return 0




def CloseSession(driver):
    driver.quit


if __name__ == '__main__':
 main()




