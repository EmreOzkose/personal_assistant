import sys

import flask
import time
import cv2
import numpy
import json
from queue import Queue
from os.path import join as p_join
from assistant import Assistant
from utils import *

app = flask.Flask(__name__)


@app.route('/personal_assistan', methods=['GET', 'POST'])
def find_navigation():
    """
    :return:
    """

    """addresses = flask.request.values        # returns a dictionary (Map class in Java)
    start_address = addresses.get("from_address")"""

    return ""


if __name__ == "__main__":
    config = read_config(yaml_path="../../personal_assistant_config.yaml")
    print(config)
    calendar_url = config["calendar_url"]
    assistant = Assistant(calendar_url)
    app.run(host=get_ip_address(), port=8000, debug=False)