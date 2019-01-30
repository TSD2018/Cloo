//
//  ViewController.swift
//  cloomap
//
//  Created by Shilpa Shettar on 22/01/19.
//  Copyright © 2019 Shilpa Shettar. All rights reserved.
//

import UIKit
import CoreLocation


class ViewController: UIViewController, CLLocationManagerDelegate {
    
    @IBOutlet weak var mn_txt_currloc: UITextField!
    @IBOutlet var mn_btn_refreshloc: UIView!
    @IBOutlet var mn_btn_addloo: UIView!
    @IBOutlet var mn_tblv_listloo: UIView!
    
    let locationManager = CLLocationManager()
    
    // You don't need to modify the default init(nibName:bundle:) method.

    override func viewDidLoad() {
        super.viewDidLoad()
        
        //For use when the app is in the background
        locationManager.requestAlwaysAuthorization()
        
        if (CLLocationManager.locationServicesEnabled())
        {
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.startUpdatingLocation()
        }
    }

    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation])
    {
        if let location = locations.first
        {
        /* you can use these values*/
        let lat = location.coordinate.latitude
        let long = location.coordinate.longitude
            print(lat)
            print(long)
        //let lat = String(format: ":2lf" ,location.coordinate.latitude)
        //example let combined2 = "\(string1) \(string2)"
        self.mn_txt_currloc.text = "\(lat)  ,   \(long)"
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus)
    {
        if(status == CLAuthorizationStatus.denied)
        {
            showLocationDisabledPopUp()
        }
    }
    
    // Show the popup to the user if we have been denied access
    func showLocationDisabledPopUp()
    {
        let alertController = UIAlertController(title: "Background Location Access Disabled",
                                                message: "We need your location - Team Cloo",
                                                preferredStyle: .alert)
        
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        alertController.addAction(cancelAction)
        
        let openAction = UIAlertAction(title: "Open Settings", style: .default) { (action) in
            if let url = URL(string: UIApplication.openSettingsURLString) {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
            }
        }
        alertController.addAction(openAction)
        
        self.present(alertController, animated: true, completion: nil)
    }
    override func didReceiveMemoryWarning()
    {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

