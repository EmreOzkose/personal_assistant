import sys

import flask
import time
import json
from os.path import join as p_join
from assistant import Assistant
from utils import *
import json

app = flask.Flask(__name__)


@app.route('/personal_assistan', methods=['GET', 'POST'])
def find_navigation():
    """
    :return:
    """

    """addresses = flask.request.values        # returns a dictionary (Map class in Java)
    start_address = addresses.get("from_address")"""
    event_list = assistant.read_today_events()
    print(event_list)

    json_events = json.dumps(event_list)

    return json_events


if __name__ == "__main__":
    config = read_config(yaml_path="../../personal_assistant_config.yaml")

    calendar_url = config["calendar_url"]
    assistant = Assistant(calendar_url)

    app.run(host=get_ip_address(), port=5000, debug=False)
