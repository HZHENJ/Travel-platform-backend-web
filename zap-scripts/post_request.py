import requests

def zap_started(zap, target):
    """
    ZAP 启动后，手动发送 POST 请求，以便 ZAP 发现这个 API 端点。
    """
    headers = {'Content-Type': 'application/json'}
    data = {
        "email": "b@qq.com",
        "password": "123"
    }

    print(f"Sending POST request to {target} with payload: {data}")

    try:
        response = requests.post(target, json=data, headers=headers)
        print(f"Response Code: {response.status_code}")
        print(f"Response Body: {response.text}")
    except Exception as e:
        print(f"Failed to send POST request: {e}")
