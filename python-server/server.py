import winsound
from fastapi import FastAPI, HTTPException
import uvicorn

app = FastAPI()

@app.get("/notify/startup")
@app.post("/notify/startup")
async def notify_startup():
  """マイクラの起動通知を受け取るエンドポイント"""
  print("🔔 マイクラの起動通知を受け取りました！")

  try:
    winsound.Beep(1000, 200)  # 1回目
    winsound.Beep(1500, 300)  # 2回目（高めの音）
  except Exception as e:
    print(f"⚠️ 音の再生に失敗しました: {e}")

  return {"status": "success", "message": "通知を受け取り、音を鳴らしました！"}


if __name__ == "__main__":
  # ローカルのポート 8000番 でサーバーを起動します
  print("🚀 Python通知サーバーを起動します... (http://localhost:8000/notify/startup)")
  uvicorn.run(app, host="0.0.0.0", port=8000)