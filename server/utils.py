import yaml
import os
import socket


def read_config(yaml_path):
    a_yaml_file = open(yaml_path)
    parsed_yaml_file = yaml.load(a_yaml_file, Loader=yaml.FullLoader)

    return parsed_yaml_file


def get_ip_address():
    os_info = os.uname()
    os_name = os_info.sysname

    hostname = socket.gethostname()

    if os_name == "Linux":
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        try:
            # doesn't even have to be reachable
            s.connect(('10.255.255.255', 1))
            ip_address = s.getsockname()[0]
        except:
            ip_address = '127.0.0.1'
        finally:
            s.close()

    else:
        ip_address = socket.gethostbyname(hostname)

    return ip_address
