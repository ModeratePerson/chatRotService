import requests
import os

api_key = os.getenv("DASHSCOPE_API_KEY")
url = 'https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation'
headers = {'Content-Type': 'application/json',
           'Authorization': f'Bearer {api_key}'}

def get_response(last_messages):
    body = {
        'model': 'qwen-turbo',
        "input": {
            "messages": last_messages
        },
        "parameters": {
            "result_format": "message"
        }
    }
    response = requests.post(url, headers=headers, json=body)
    return response.json()