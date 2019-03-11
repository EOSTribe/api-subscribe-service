from tempfile import mkstemp
from flask import Flask, Response, request
import os
from shutil import move

app = Flask(__name__)
subscriber_map_filename = '/etc/haproxy/maps/subscribers.map'

@app.route('/setmap', methods=['POST','OPTIONS','GET'])
def setmap():
    apikey = request.args.get("apikey")
    sla = request.args.get('sla')
    add_key(apikey=apikey,sla=sla)
    return Response(status=200)

@app.route('/delmap')
def delmap():
    apikey = request.args.get("apikey")
    remove_apikey(apikey=apikey)
    return Response(status=200)


def add_key(apikey,sla):
    if apikey in open(subscriber_map_filename).read():
        replace_apikey(apikey=apikey,sla=sla)
    else:
        with open(subscriber_map_filename,'a+') as f:
          f.write(apikey+" "+sla+"\n")
          f.close

def replace_apikey(apikey,sla):
    fh,target_file_path = mkstemp()
    with open(target_file_path, 'w') as target_file:
        with open(subscriber_map_filename, 'r') as source_file:
            for line in source_file:
                if apikey in line:
                    target_file.write(apikey+" "+sla+"\n")
                else:
                    target_file.write(line)
    os.remove(subscriber_map_filename)
    move(target_file_path, subscriber_map_filename)
    os.chmod(subscriber_map_filename, 0o644)

def remove_apikey(apikey):
    fh,target_file_path = mkstemp()
    with open(target_file_path, 'w') as target_file:
        with open(subscriber_map_filename, 'r') as source_file:
            for line in source_file:
                if apikey in line:
                    pass
                else:
                    target_file.write(line)
    os.remove(subscriber_map_filename)
    move(target_file_path, subscriber_map_filename)
    os.chmod(subscriber_map_filename, 0o644)





if __name__ == '__main__':
    app.run(host='127.0.0.1', port=5500, debug=True)
    app.config['JSON_AS_ASCII'] = False