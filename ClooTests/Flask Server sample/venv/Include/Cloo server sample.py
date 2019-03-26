from flask import Flask, request, jsonify, render_template
from flask_cors import CORS, cross_origin
from firebase import firebase
import os, json
from geopy import distance
from flask import send_from_directory, send_file


fbase = firebase.FirebaseApplication('https://cloo2019-4d1a2.firebaseio.com/', None)  # Open DB connection
#fbase = firebase.FirebaseApplication('https://naveentestproj-7a7c1.firebaseio.com/', None)  # Open DB connection

app = Flask(__name__)
CORS(app)

HostedServer = True

if HostedServer == True:
    #Use this for running the server on a hosted server (PythonAnywhere server)
    UPLOAD_FOLDER = "/home/ClooServerR1/mysite/uploads/"
else:
    # Use this for running the server on a local system
    UPLOAD_FOLDER = os.path.basename('uploads')

app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


#This request is to fetch the default html file from the server to be displayed on the browser.
@app.route('/')
def index():
    #Unit test function calls!!
    #AddNewRating()
    #AddNewToilet()
    #return render_template("Cloo Admin.html")
    return ("OK")

#This request is to fetch Toilet details from database and send to the client.
@app.route('/result',methods = ['POST', 'GET'])
def result():
   if request.method == 'GET':
      obj1 = fbase.get('/ToiletMaster', None)
      print(obj1)
      return (jsonify(obj1))


#This request is to fetch Toilet details based on current lat, long and radius. Finding nearest toilets...
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
       if HostedServer == True:
           f = app.config['UPLOAD_FOLDER'] + file.filename
       else:
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
           if HostedServer == True:
               return (send_file(filenm, as_attachment=True))
           else:
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
       UserComments = (request.form.get('Comments'))
       GenderType = (request.form.get('GenderType'))
       #looName = "New Toilet from Flask server8"
       #toiletaddr = "Eco-friendly toilet address8"
       #Lat = "12.7773488"
       #Lng = "77.1234288"
       #UserRating = "1"
       #UserComments="Comment from user8"
       #GenderType="0"
       NewToilet = {"toiletName":looName,"address":toiletaddr,"lat":float(Lat),"lng":float(Lng),"userRating":0,
                        "alt":0,"contact":"","genderType":int(GenderType),"lastCleanedBy":"","lastCleanedTimeStamp":"",
                        "maintainedBy":"","sponsor_image":"","toiletAccess":0,"toiletOwnerBy":"",
                        "toiletSponsor":"","toiletType":0,"numberOfRatings":0,
                        "numberOfRatingsLifeTime":0,"ratingSum":0,"ratingSumLifeTime":0,"toiletId":""}

       res = fbase.post('/ToiletMaster/', NewToilet)
       looid = res['name']
       fbase.patch('/ToiletMaster/'+looid,{'toiletId': looid})
       print (fbase.get('/ToiletMaster/' + looid, None))
       AddRatingtoDB(looid,UserRating,UserComments)
       return ("OK")


#This request is to add a new rating for a specific toilet in the database.
@app.route('/AddNewRating',methods = ['GET', 'POST'])
def AddNewRating():
   if request.method == 'POST':
       looid = (request.form.get('LooId'))
       UserRating = (request.form.get('Rating'))
       UserComments = (request.form.get('Comments'))
       #looid = "-LaPSmSv68a7uQgWnjXx"
       #UserRating = "4"
       #UserComments = "Comment 7one"
       AddRatingtoDB(looid,UserRating,UserComments)
       return ("OK")


def AddRatingtoDB(looid,UserRating,UserComments):
    if (UserRating != ""):
        NewRating = {"toiletId": looid, "timeString": "", "userComments": UserComments, "userId": "",
                     "userRating": int(UserRating),
                     "ratingId": 0}
        res = fbase.post('/ToiletRating/' + looid, NewRating)
        fbase.patch('/ToiletRating/' + looid + '/' + res['name'], {'ratingId': res['name']})
        res1 = fbase.get('/ToiletRating', None)

        numberOfRatings = fbase.get('/ToiletMaster/' + looid, 'numberOfRatings')
        numberOfRatingsLifeTime = fbase.get('/ToiletMaster/' + looid, 'numberOfRatingsLifeTime')
        ratingSum = fbase.get('/ToiletMaster/' + looid, 'ratingSum')
        ratingSumLifeTime = fbase.get('/ToiletMaster/' + looid, 'ratingSumLifeTime')
        fbase.patch('/ToiletMaster/'+looid,{'numberOfRatings': numberOfRatings+1,'numberOfRatingsLifeTime':numberOfRatingsLifeTime+1,
                                            'ratingSum':ratingSum+int(UserRating),'ratingSumLifeTime':ratingSumLifeTime+int(UserRating)})

if HostedServer != True:
    if __name__ == '__main__':
        app.run(host='0.0.0.0',port=5000,debug = True)

