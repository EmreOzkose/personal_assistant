

class Assistant:
    def __init__(self, calendar_url):
        self.calendar_url = calendar_url

    def read_today_calendar(self):
        from ics import Calendar
        import requests

        url = self.calendar_url
        c = Calendar(requests.get(url).text)

        e = list(c.timeline)[0]
        print("Event '{}' started {}".format(e.name, e.begin.humanize()))
        # "Event 'Workshop Git' started 2 years ago"

        today = list(c.timeline.today())[0]
        print(today.name)
        print(today.begin.humanize())
