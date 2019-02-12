from flask import Flask, request, jsonify, render_template
from flask_cors import CORS, cross_origin
from firebase import firebase
import os
from flask import send_from_directory, send_file


fbase = firebase.FirebaseApplication('https://naveentestproj-7a7c1.firebaseio.com/', None)  # Open DB connection

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
      #obj = ["Cloo1", "Cloo2", "Cloo3"]
      #obj = {"T1":"Cloo1","T2":"Cloo2","T3":"Cloo3"}
      obj = {"Toilets": [
          {
              "name": "Cloo1",
              "addr": "addr1",
              "Id": "Id1"
          },
          {
              "name": "Cloo2",
              "addr": "addr2",
              "Id": "Id2"
          },
          {
              "name": "Cloo3",
              "addr": "addr3",
              "Id": "Id3"
          }
      ]
      }

      obj1 = fbase.get('/ToiletMaster', None)
      return (jsonify(obj1))


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
       return ("respstr")


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


if __name__ == '__main__':
   app.run(debug = True)

