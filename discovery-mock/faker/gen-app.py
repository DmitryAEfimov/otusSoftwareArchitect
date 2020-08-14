from flask import Flask, request, abort, redirect

app = Flask(__name__)

@app.route("/data")
def data_generator():
    from generator import FakeData
    items_cnt = request.args.get('items_cnt')

    return FakeData().generate(items_cnt)


if __name__ == "__main__":
    app.run(host='0.0.0.0', port='80', debug=True)