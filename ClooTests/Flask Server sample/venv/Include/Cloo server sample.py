from flask import Flask, request, jsonify, render_template
from flask_cors import CORS, cross_origin
from firebase import firebase
import os, json
from geopy import distance
from flask import send_from_directory, send_file


fbase = firebase.FirebaseApplication('https://cloo2019-4d1a2.firebaseio.com/', None)  # Open DB connection

app = Flask(__name__)
CORS(app)

UPLOAD_FOLDER = os.path.basename('uploads')
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


#This request is to fetch the default html file from the server to be displayed on the browser.
@app.route('/')
def index():
   return render_template("Cloo Admin.html")

#This request is to fetch Toilet details from database and send to the client.
@app.route('/result',methods = ['POST', 'GET'])
def result():
   if request.method == 'GET':
      obj1 = fbase.get('/ToiletMaster', None)
      print(obj1)
      return (jsonify(obj1))


#This request is to fetch Toilet details based on current lat long and radius. Finding nearest toilets...
@app.route('/resultsub',methods = ['GET', 'POST'])
def resultsub():
   if request.method == 'POST':
          Lat = (request.form.get('Lat'))
          print("Lat= " + Lat)
          Lng = (request.form.get('Lng'))
          print("Long= " + Lng)
          Radius = (request.form.get('Radius'))
          print("Radius =" + Radius)
          obj1 = fbase.get('/ToiletMaster', None)
          RefLatLng = (float(Lat),float(Lng))
          #filtered_dict = list(filter(lambda item: item['numberOfRatings'] >= 2, obj1.values()))
          filtered_dict = list(filter(lambda item: distance.great_circle(RefLatLng, (item['lat'],item['lng'])).kilometers <= float(Radius), obj1.values()))
          #filtered_dict = list(filter(lambda item: , obj1.values()))
          #filtered_dict = {x: y for x,y in obj1.items() if distance.great_circle(RefLatLng, (y['lat'],y['lng'])).kilometers <= float(Radius)}
          print(filtered_dict)
          return (jsonify(filtered_dict))


#This request is to update the sponsor name as sent from the client.
@app.route('/result1',methods = ['GET', 'POST'])
def result1():
   if request.method == 'POST':
       looName= (request.form.get('SelLoo'))
       print("Toilet Name= " +looName)
       looid =(request.form.get('LooId'))
       print("Toilet Id= " +looid)
       sponsor =  (request.form.get('SpName'))
       print("Sponsor =" +sponsor)
       fbase.patch('/ToiletMaster/'+looid, {'toiletSponsor': sponsor})
       return ("OK")


#This request is to upload sponsor logo to the server and store the path in firebase for a selected toilet id.
@app.route('/result2',methods = ['GET', 'POST'])
def result2():
   if request.method == 'POST':
       file = request.files['photos']
       f = os.path.join(app.config['UPLOAD_FOLDER'], file.filename)
       file.save(f)
       print(f)
       looid =(request.form.get('LooId'))
       print("Toilet Id= " +looid)
       fbase.patch('/ToiletMaster/'+looid, {'sponsor_image': f})
       return ("OK")
       #return (send_from_directory(app.config['UPLOAD_FOLDER'],file.filename, as_attachment=True))


#This request is to send the sponsor logo based on selected toilet id to the client.
@app.route('/result3',methods = ['GET', 'POST'])
def result3():
   if request.method == 'POST':
       looid =(request.form.get('LooId'))
       print("Toilet Id= " +looid)

       #Replace the file name with the name you get from the firebase database for a specific LooId.
       #filenm = "<filename from firebase>"
       filenm = fbase.get('/ToiletMaster/'+looid, 'sponsor_image')
       print(filenm)

       #return (send_from_directory(app.config['UPLOAD_FOLDER'],
                                  #filenm, as_attachment=True))
       if filenm != None:
           return (send_file(app.root_path+"\\"+filenm, as_attachment=True))
       else:
           return ("NOTOK")


#This request is to add a new toilet in the database.
@app.route('/AddNewToilet',methods = ['GET', 'POST'])
def AddNewToilet():
   if request.method == 'POST':
       looName =(request.form.get('SelLoo'))
       toiletaddr = (request.form.get('ToiletAddr'))
       Lat = (request.form.get('Lat'))
       Lng = (request.form.get('Lng'))
       UserRating = (request.form.get('Rating'))
       #looName = "New Toilet from Flask server"
       #toiletaddr = "Eco-friendly toilet address"
       #Lat = "12.777342"
       #Lng = "77.123423"
       #UserRating = "5"
       NewToilet = {"toiletName":looName,"address":toiletaddr,"lat":Lat,"lng":Lng,"userRating":UserRating}
       filenm = fbase.post('/ToiletMaster/', NewToilet)
       return ("OK")


if __name__ == '__main__':
   app.run(host='0.0.0.0',port=5000,debug = True)

