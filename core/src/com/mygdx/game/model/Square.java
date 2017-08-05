package com.mygdx.game.model;

public class Square  {
	public enum Type {sqEmpty (0),
		sqChunyang (1),
		sqCangjian(6),
		sqTiance(2),
		sqShaolin(3),
		sqWanhua(4),
		sqQixiu(5),
		sqWudu(7), sqTangmen(8), sqGaibang(10), sqMingjiao(9), sqCangyun(11), sqChangge(12), sqBadao(13);
		private int value;

		public int getValue() {
			return value;
		}

		Type(int value){
			this.value = value;
		}
	};

	public int origY;
	public int destY;
	public boolean mustFall;
	private Type _type;

	public Square(Type type) {
		_type = type;
		mustFall = false;
	}

	public Square(Square other) {
		_type = other._type;
		origY = other.origY;
		destY = other.destY;
		mustFall = other.mustFall;
	}

	public Type getType() {
		return _type;
	}

	public void setType(Type type) {
		_type = type;
	}

	public boolean equals(Square other) {
		return other._type == _type;
	}

	public boolean equals(Type type) {
		return type == _type;
	}

	public static Type numToType(int num) {
		switch (num) {
			case 1:
				return Type.sqChunyang;
			case 2:
				return Type.sqTiance;
			case 3:
				return Type.sqShaolin;
			case 4:
				return Type.sqWanhua;
			case 5:
				return Type.sqQixiu;
			case 6:
				return Type.sqCangjian;
			case 7:
				return Type.sqWudu;
			case 8:
				return Type.sqTangmen;
			case 9:
				return Type.sqMingjiao;
			case 10:
				return Type.sqGaibang;
			case 11:
				return Type.sqCangyun;
			case 12:
				return Type.sqChangge;
			case 13:
				return Type.sqBadao;
			default:
				return Type.sqEmpty;
		}
	}
}
