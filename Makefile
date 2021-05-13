all:
	docker build -t stackoverflow-clone .
	docker run -dp 3000:4000 -e PORT=4000 stackoverflow-clone