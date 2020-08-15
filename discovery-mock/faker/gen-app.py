import json, os
from flask import Flask, Response, request
from generator import Item

app = Flask(__name__)


@app.route("/faker/data")
def data_generator():
    from generator import FakeData
    items_cnt = request.args.get('items_cnt')

    items = toJson(FakeData().generate(items_cnt))
    response = Response(response=json.dumps(items),
                        status=200,
                        mimetype="application/json")
    return response


@app.route("/faker/health")
def health():
    return '{"status": "ok"}'


def toJson(items: Item):
    return {
        "items": [
            {
                "domen": item.domen,
                "ipv4": item.ipv4,
                "model": item.model,
                "status": item.status
            }
            for item in items
        ]
    }


if __name__ == "__main__":
    port = os.environ["PORT"]
    app.run(host='0.0.0.0', port=port, debug=True)
