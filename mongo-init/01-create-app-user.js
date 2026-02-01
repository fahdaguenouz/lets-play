// This script runs ONLY on the first initialization of the Mongo volume.
// If you already have data in the volume, it will NOT re-run (important).

const dbName = process.env.MONGO_DB || "lets_play";
const appUser = process.env.MONGO_APP_USERNAME || "letsplay";
const appPass = process.env.MONGO_APP_PASSWORD || "letsplaypass";

db = db.getSiblingDB(dbName);

db.createUser({
  user: appUser,
  pwd: appPass,
  roles: [
    { role: "readWrite", db: dbName },
    { role: "dbAdmin", db: dbName },
  ],
});

print(`✅ Created user '${appUser}' on db '${dbName}'`);
