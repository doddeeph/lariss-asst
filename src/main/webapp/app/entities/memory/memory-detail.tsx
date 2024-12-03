import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './memory.reducer';

export const MemoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const memoryEntity = useAppSelector(state => state.memory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="memoryDetailsHeading">
          <Translate contentKey="larissAsstApp.memory.detail.title">Memory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissAsstApp.memory.name">Name</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.name}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="larissAsstApp.memory.value">Value</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.value}</dd>
        </dl>
        <Button tag={Link} to="/memory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/memory/${memoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MemoryDetail;
