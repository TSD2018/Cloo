import time
from appium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from appium.webdriver.common.touch_action import TouchAction
import unittest
import csv
import sys


def SelectLooFromList(driver,LooIndex):
    try:
        element = driver.find_elements_by_class_name('android.widget.TextView')[LooIndex]
        CleanedOn = element.get_attribute("text")
        print(CleanedOn)
        if "not known" in CleanedOn:
            print("No cleaned date for the selected toilet")
        # result = CleanedOn.find("not known")
        element.click()
        time.sleep(9)
    except:
        return -1


def SelectRating(driver,Xcord,Ycord):
    try:
        element = driver.find_element_by_class_name('android.widget.RatingBar')
        actions = ActionChains(driver)
        actions.move_to_element_with_offset(element, Xcord, Ycord)
        actions.perform()
        actions.click()
        actions.perform()
    except:
        return -1


def ClickButton(driver,ButtonName):
    try:
        element = driver.find_element_by_id(ButtonName).click()
        time.sleep(3)
    except:
        return -1


def SelectCheckBox(driver,boxvalue):
    print(boxvalue)
    element = driver.find_element_by_id(boxvalue).click()


def AddTextById(driver,textelem,textval):
    element = driver.find_element_by_id(textelem)
    element.send_keys(textval)
    time.sleep(1)


def SelectFromDropDown(driver,boxvalue,valueid):
    element = driver.find_element_by_id(boxvalue)
    element.click()
    time.sleep(2)
    element = driver.find_elements_by_class_name('android.widget.TextView')[valueid]
    element.click()
    time.sleep(2)

def ScrollSimulation(driver,grpvalue,x1,y1,x2,y2):
    time.sleep(2)
    element = driver.find_element_by_class_name(grpvalue)
    TouchAction(driver).press(element, x1, y1).wait(1000).move_to(element, x2, y2).release().perform()
    time.sleep(2)


def WriteResult(writer,TestcaseNum,TestResult,TestRemarks):
    writer.writerow({'Test case #': TestcaseNum, 'Test Result': TestResult, 'Remarks': TestRemarks})
    return (-1)
