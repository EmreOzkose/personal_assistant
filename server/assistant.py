from ics import Calendar
import requests


class Assistant:
    def __init__(self, calendar_url):
        self.calendar_url = calendar_url

    def read_today_events(self):
        url = self.calendar_url
        c = Calendar(requests.get(url).text)

        e = list(c.timeline)[0]
        # "Event 'Workshop Git' started 2 years ago"

        today_list = list(c.timeline.today())

        event_list = []
        for today_i in today_list:
            name = today_i.name
            start_time = today_i.begin.humanize()
            event_list.append({"name": name, "start_time": start_time})

        return event_list
