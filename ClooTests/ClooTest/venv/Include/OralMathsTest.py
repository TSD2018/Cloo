import time
from appium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
import unittest
import csv
import sys

def main():
    #Initialize driver session with the device and get driver handle to use later in the code.
    driver = InitializeSession()

    #Create CSV file for output results
    csv_file = open('Cloo Test Results.csv', mode='w')
    fieldnames = ['Test case #', 'Test Result', 'Remarks']
    writer = csv.DictWriter(csv_file, fieldnames=fieldnames)
    writer.writeheader()

    #Test for selecting a loo, see directions and update rating.
    #Test1(driver,writer)

    #Test for adding new loo information.
    #Test2(driver,writer)

    #Test for adding cleaning details
    #Test3(driver,writer)

    #Test for modifying existing Janitor info
    #Test4(driver,writer)

    #Test OralMaths app to get values displayed in the text view
    Test5(driver)

    #Close CSV file
    csv_file.close()

    #Close test session with the server.
    CloseSession(driver)

def InitializeSession():
    DesiredCaps = {
        'platformName': 'Android',
        'platformVersion': '9',
        'deviceName': 'Android Emulator',
        'automationName': 'UiAutomator2',
        'app': "E:\Data\OralMaths\OralMaths\App\Build\Outputs\Apk\Debug\App-debug.apk",
        'appWaitDuration': 30000,
        'appPackage': 'com.example.oralmaths',
        'appActivity': '.MainActivity',
        'deviceReadyTimeout': 5,
        'avd': 'Pixel_API_28'

    }

    """DesiredCaps = {
        'platformName': 'Android',
        'platformVersion': '9',
        'deviceName': 'Android Emulator',
        'automationName': 'UiAutomator2',
        'app': "E:\Data\cloo\Cloo-master\Cloo\Cloo2019\app\build\outputs\apk\debug\App-debug.apk",
        'appWaitDuration': 30000,
        'appPackage': 'com.example.Cloo2019',
        'appActivity': '.MainActivityLocateLoo',
        'deviceReadyTimeout': 5,
        'avd': 'Pixel_API_28'

    }"""

    driver = webdriver.Remote('http://127.0.0.1:4723/wd/hub', DesiredCaps)
    return driver

def Test1(driver,writer):
    TestResult = ""
    #Select Allow button to access location services if prompted.
    try:
        element = driver.find_element_by_id('com.android.packageinstaller:id/permission_allow_button')
        element.click()
        time.sleep(9)
    except:
        TestResult = "Test 1 failed: Could not locate allow button in the flow."
        writer.writerow({'Test case #': 'Test 1', 'Test Result': 'FAILED', 'Remarks': TestResult})
        return (-1)
        #print(TestResult,flush=True)


    # Invoke a loo location from the list view to see directions and update rating and/or comments.
    # Invokes second record from the list. In this screen, there are many textviews. Instance 4 corresponds to second item.
    #element = driver.find_element_by_android_uiautomator('new UiSelector().className("android.widget.TextView").instance(4)')
    try:
        element = driver.find_elements_by_class_name('android.widget.TextView')[4]
        element.click()
        time.sleep(9)
    except:
        TestResult = "Test 1 failed: Could not locate the toilet in the list."
        writer.writerow({'Test case #': 'Test 1', 'Test Result': 'FAILED', 'Remarks': TestResult})
        return (-1)
        #print(TestResult,flush=True)



    #Select Direction button to see the map
    try:
        element = driver.find_element_by_id('com.example.cloo2019:id/button_Navigate')
        element.click()
        time.sleep(5)
    except:
        TestResult = "Test 1 failed: Could not locate the back button in the navigation window."
        writer.writerow({'Test case #': 'Test 1', 'Test Result': 'FAILED', 'Remarks': TestResult})
        return (-1)

    #Select Back button to choose rating, submit and close.
    driver.press_keycode(4)
    time.sleep(10)
    # Update rating and comments
    try:
        element = driver.find_element_by_class_name('android.widget.RatingBar')
        actions = ActionChains(driver)
        # Co-ordinates for Ratingbar
        # 5: 580,120
        # 4: 460,120
        # 3: 360,120
        # 2: 240,120
        # 1: 100,120
        actions.move_to_element_with_offset(element, 360, 120)
        actions.perform()
        actions.click()
        actions.perform()
    except:
        TestResult = "Test 1 failed: Could not locate the Rating bar button from the navigation window."
        writer.writerow({'Test case #': 'Test 1', 'Test Result': 'FAILED', 'Remarks': TestResult})
        return (-1)

    element = driver.find_element_by_id('com.example.cloo2019:id/button_submit_close')
    element.click()
    time.sleep(5)
    # Select Back button to go back to Loo list view screen.
    driver.press_keycode(4)
    time.sleep(2)

def Test2(driver,writer):
    #Add new loo rating and comments
    element = driver.find_element_by_class_name('android.widget.ImageButton')
    element.click()
    time.sleep(5)
    element = driver.find_element_by_class_name('android.widget.RatingBar')
    actions = ActionChains(driver)
    #Co-ordinates for Ratingbar
    #5: 580,120
    #4: 460,120
    #3: 360,120
    #2: 240,120
    #1: 100,120
    actions.move_to_element_with_offset(element,100,120)
    actions.perform()
    actions.click()
    actions.perform()
    element = driver.find_element_by_class_name('android.widget.EditText')
    element.send_keys('Add comments to this rating.')
    time.sleep(1)
    element = driver.find_element_by_id('com.example.cloo2019:id/button_Submit')
    element.click()
    TestResult = "Test 2 passed the flow of adding toilets"
    writer.writerow({'Test case #': 'Test 2', 'Test Result': 'PASSED', 'Remarks': TestResult})

def Test3(driver,writer):
    #Add cleaning details for 'Basics' section and remarks of an existing loo
    #element = driver.find_element_by_android_uiautomator(
    #    'new UiSelector().className("android.widget.TextView").instance(4)')
    time.sleep(2)
    element = driver.find_elements_by_class_name('android.widget.TextView')[4]
    element.click()
    time.sleep(5)
    element = driver.find_element_by_id('com.example.cloo2019:id/buttonClean').click()
    time.sleep(2)
    element = driver.find_element_by_id('com.example.cloo2019:id/checkBoxWater').click()
    element = driver.find_element_by_id('com.example.cloo2019:id/checkBoxElectricity').click()
    element = driver.find_element_by_id('com.example.cloo2019:id/checkBoxStinks').click()
    element = driver.find_element_by_id('com.example.cloo2019:id/checkBoxPlumbing').click()
    element = driver.find_element_by_id('com.example.cloo2019:id/editTextOthers')
    element.send_keys('All basics check boxes ticked')
    time.sleep(1)
    driver.press_keycode(4)
    time.sleep(2)
    TestResult = "Test 3 passed the flow of adding clean record details."
    writer.writerow({'Test case #': 'Test 1', 'Test Result': 'PASSED', 'Remarks': TestResult})



def Test4 (driver,writer):
    # Select Allow button to access location services if prompted.
    #element = driver.find_element_by_id('com.android.packageinstaller:id/permission_allow_button')
    #element.click()
    #time.sleep(9)

    # Invoke a loo location from the list view to see directions and update rating and/or comments.
    # Invokes second record from the list. In this screen, there are many textviews. Instance 4 corresponds to second item.
    # element = driver.find_element_by_android_uiautomator('new UiSelector().className("android.widget.TextView").instance(4)')
    #element = driver.find_elements_by_class_name('android.widget.TextView')[4]
    #element.click()
    #time.sleep(9)

    #Update existing Janitor details.
    element = driver.find_element_by_id('com.example.cloo2019:id/button_Janitor').click()
    time.sleep(5)

    #Select Toilet access from the drop down.
    element = driver.find_element_by_id('com.example.cloo2019:id/spinner_toiletAccess')
    element.click()
    time.sleep(2)
    element = driver.find_elements_by_class_name('android.widget.TextView')[2]
    element.click()
    time.sleep(2)

    #Select Gender from the drop down
    element = driver.find_element_by_id('com.example.cloo2019:id/spinner_Gender')
    element.click()
    time.sleep(2)
    element = driver.find_elements_by_class_name('android.widget.TextView')[2]
    element.click()
    time.sleep(2)

    #Save the details
    element = driver.find_element_by_id('com.example.cloo2019:id/buttonSave').click()
    time.sleep(2)

    #Move back to list view screen
    driver.press_keycode(4)
    driver.press_keycode(4)
    TestResult = "Test 4 passed the flow of updating records for the selected toilet."
    writer.writerow({'Test case #': 'Test 4', 'Test Result': 'PASSED', 'Remarks': TestResult})


def Test5(driver):
    element = driver.find_elements_by_class_name('android.widget.TextView')[1]
    num1 = element.get_attribute("text")
    print(num1)
    element = driver.find_elements_by_class_name('android.widget.TextView')[3]
    num2 = element.get_attribute("text")
    print(num2)
    sumtotal = int(num1)+int(num2)
    print (sumtotal)
    #element.__setattr__("text",sumtotal)
    element = driver.find_element_by_class_name("android.widget.EditText")
    element.click()
    element.send_keys(sumtotal)
    #element.send_keys(0x42)
    #driver.press_keycode(0x42)
    #driver.keyevent(0x42)
    #driver.sendKeyEvent(66)
    driver.execute_script('mobile: performEditorAction', {'action': 'done'})
    element = driver.find_elements_by_class_name('android.widget.TextView')[5]
    PassCount = element.get_attribute("text")
    print(PassCount)
    element = driver.find_elements_by_class_name('android.widget.TextView')[6]
    print(element.get_attribute("text"))

    if (int(PassCount) < 10):
        Test5(driver)



def CloseSession(driver):
    driver.quit


if __name__ == '__main__':
 main()




