db = db.getSiblingDB("lets_play");

db.createUser({
  user: "letsplay",
  pwd: "letsplaypass",
  roles: [
    { role: "readWrite", db: "lets_play" },
    { role: "dbAdmin",  db: "lets_play" }
  ]
});
