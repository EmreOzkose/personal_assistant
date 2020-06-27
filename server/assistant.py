
# https://icspy.readthedocs.io/en/stable/api.html
#
# =========== * ===========
from ics import Calendar
import requests
import datetime
from datetime import time
from datetime import date


class Assistant:
    def __init__(self, calendar_url):
        self.calendar_url = calendar_url
        self.three_hour = datetime.timedelta(hours=3)

    def read_today_events(self):
        url = self.calendar_url
        c = Calendar(requests.get(url).text)

        e = list(c.timeline)[0]
        # "Event 'Workshop Git' started 2 years ago"

        today_list = list(c.timeline.today())

        event_list = []
        for today_i in today_list:
            name = today_i.name

            start_date_time = datetime.datetime(today_i.begin.date().year,
                                                today_i.begin.date().month,
                                                today_i.begin.date().day,
                                                today_i.begin.time().hour,
                                                today_i.begin.time().minute,
                                                today_i.begin.time().second)
            start_date_time += self.three_hour

            start_time = "{} {}".format(start_date_time.hour, start_date_time.minute)
            event_list.append({"name": name, "start_time": start_time})

        return event_list
