from flask import Flask, render_template, redirect, jsonify, request, make_response
from datetime import datetime
from flask_mongoengine import MongoEngine

DB_URI = 'mongodb+srv://admindb:IBHWd0u2oyGWp0kW@cluster0.rio81.mongodb.net/myappointmentdb?retryWrites=true&w=majority'
STD_DATE_FORMAT = '%m-%d-%Y %H:%M'

"""Set up application"""
app = Flask(__name__)
app.config['MONGODB_HOST'] = DB_URI

db = MongoEngine()
db.init_app(app)

wsgi_app = app.wsgi_app

class Appointments(db.Document):
    appointId = db.SequenceField(primary_key=True)
    title = db.StringField()
    startTime = db.DateTimeField(default=datetime.utcnow)
    endTime = db.DateTimeField(default=datetime.utcnow)
    isAllDay = db.BooleanField()
    location = db.StringField()
    description = db.StringField()
    displayStartTime = db.StringField()
    displayEndTime = db.StringField()

    meta = {
        'collection': 'Appointments',
    }


@app.route('/')
@app.route('/appointments')
def get_calendar():
    calendar = Appointments.objects
    return jsonify(calendar)

@app.route('/appointments/<int:appId>')
def get_calendar_by_id(appId):

    calendar = Appointments.objects(appointId=appId).first()

    if not calendar:
        return jsonify({'error': 'data not found'})

    return jsonify(calendar)

@app.route('/appointments/search', methods=['POST'])
def search_appointments():
    searchTerm = request.get_json()

    startDate = datetime.strptime(searchTerm["startTime"], STD_DATE_FORMAT)
    endDate = datetime.strptime(searchTerm["endTime"], STD_DATE_FORMAT)

    calendar = list(Appointments.objects(startTime__gte=startDate, endTime__lte=endDate))
    return jsonify(calendar)


@app.route('/appointments/add', methods=['POST'])
def add_appointments():
    appointment = request.get_json()

    newAppoint = Appointments(title=appointment["title"],
                    startTime=datetime.strptime(appointment["displayStartTime"], STD_DATE_FORMAT),
                    displayStartTime=appointment["displayStartTime"],
                    endTime=datetime.strptime(appointment["displayEndTime"], STD_DATE_FORMAT),
                    displayEndTime=appointment["displayEndTime"],
                    isAllDay=appointment["isAllDay"],
                    location=appointment["location"],
                    description=appointment["description"])

    newAppoint.save()
    return jsonify(newAppoint)


@app.route('/appointments/edit', methods=['PUT'])
def update_appointments():
    appointment = request.get_json()

    updatedApp = Appointments.objects(appointId=appointment["_id"]).first()

    if not updatedApp:
        return jsonify({'error': 'data not found'})
    else:
        updatedApp.update(title=appointment["title"],
                    startTime=datetime.strptime(appointment["displayStartTime"], STD_DATE_FORMAT),
                    displayStartTime=appointment["displayStartTime"],
                    endTime=datetime.strptime(appointment["displayEndTime"], STD_DATE_FORMAT),
                    displayEndTime=appointment["displayEndTime"],
                    isAllDay=appointment["isAllDay"],
                    location=appointment["location"],
                    description=appointment["description"])
        updatedApp = Appointments.objects(appointId=appointment["_id"]).first()
    return jsonify(updatedApp)

@app.route('/appointments/delete/<int:appId>', methods=['DELETE'])
def delete_appointments(appId):
    deletedApp = Appointments.objects(appointId=appId).first()

    if not deletedApp:
        return jsonify({'error': 'data not found'})
    else:
        deletedApp.delete()

    return jsonify({'deleted': 'OK'})


if __name__ == '__main__':
    import os
    HOST = os.environ.get('SERVER_HOST', '0.0.0.0')
    try:
        PORT = int(os.environ.get('SERVER_PORT', '5000'))
    except ValueError:
        PORT = 5000
    app.run(HOST, PORT, debug=False)
