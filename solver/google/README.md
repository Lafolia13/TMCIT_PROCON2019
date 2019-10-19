## action

遷移関連

## calculation

得点計算

## base

入力とか

使う場合は GameData::Input() の turn_time_ms の有無に気をつけてください。

## for_optuna

oputuna しようと思ったのでそれ用

## search

探索本体。虚無

main.cpp に各探索の array<Move, 8> を返す関数を入れれば動きます。

### disperse_agent

試合で使ったのはこいつ。split_agent に味方が近づきすぎないような評価を加えた。

### improve_disperse

disperse_agent を改良したつもりになったんだけど全く強くならなかったやつ。

### kizitora

kizitora さんの

### maximize_point

kizitora さんの。1ターンタイルポイント最大化とかだった気がする。

### neo_beamsearch

diperse_agent の派生のはずで、ビームサーチ自体を改良したつもりになったんだけど以下略。

### none

無を返す。全てのエージェントは停留する。

### split_agent

エージェントの数が多くなるとまともに探索ができなくなるので、

1. エージェントを小グループに分割する

1. 小グループそれぞれでビームサーチして候補を複数得る

1. 候補群を組み合わせて最も評価の高いものを次の行動とする

をしました。  
敵チームに対しても同じ計算をし、敵行動を予測もしました。

### toriaezu

素直なビームサーチを書いた気がします。
